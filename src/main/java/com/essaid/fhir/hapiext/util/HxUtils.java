package com.essaid.fhir.hapiext.util;

import com.essaid.fhir.hapiext.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HxUtils {

    private static final Logger logger = LoggerFactory.getLogger(HxUtils.class);

    public static void logMap(String header, Map<?, ?> map, Logger logger, String level) {
        if (level == null || level.isBlank()) return;

        List<?> keys = new ArrayList<>(map.keySet());
        boolean comparable = !keys.isEmpty() && keys.iterator().next() instanceof Comparable<?>;

        if (level.equalsIgnoreCase("INFO")) {
            logger.info(header);
            Stream<?> stream = map.keySet().stream();
            if (comparable) stream = stream.sorted();
            stream.forEach(key -> logger.info(key + ": " + map.get(key)));

        } else if (level.equalsIgnoreCase("DEBUG")) {
            logger.debug(header);
            Stream<?> stream = map.keySet().stream();
            if (comparable) stream = stream.sorted();
            stream.forEach(key -> logger.debug(key + ": " + map.get(key)));
        }

    }

    public static <T> Map<String, T> resolveConfigurers(Class<T> configurerClass,
                                                        Properties.Configurers configurers, ConfigurableApplicationContext applicationContext) {

        Map<String, T> resolved;
        if (configurers.getType().equals(Properties.Configurers.TYPE_MAP)) {
            resolved = new LinkedHashMap<>(resolveComponentsMap(configurers.getMap(), applicationContext).entrySet().stream().filter(e -> {
                        if (configurerClass.isInstance(e.getValue())) {
                            return true;
                        } else {
                            logger.error("Configured configurer of type: " +
                                    e.getValue().getClass().getName() + " is not an instance of required type: " +
                                    configurerClass.getName() + ". Ignoring it.");
                            return false;
                        }
                    })
                    .collect(Collectors.toMap(e -> e.getKey(), e -> (T) e.getValue())));

        } else if (configurers.getType().equals(Properties.Configurers.TYPE_CONTEXT)) {
            resolved = applicationContext.getBeansOfType(configurerClass);
        } else {
            throw new IllegalArgumentException(configurerClass.getName() + ": " + configurers);
        }
        return resolved;
    }

    private static Map<String, Object> resolveComponentsMap(Map<String, String> componentMap, ConfigurableApplicationContext applicationContext) {
//        List<T> components = new ArrayList<>();
        Map<String, Object> componentsMap = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : componentMap.entrySet()) {
            if (entry.getValue().isEmpty()) {
                componentsMap.put(entry.getKey(), null);
                continue;
            }
            Object component = null;
            if (entry.getValue().startsWith("class:")) {
                String className = entry.getValue().substring(6).trim();
                Class<?> c = null;
                try {
                    c = applicationContext.getClassLoader().loadClass(className);
                } catch (ClassNotFoundException e) {
                    logger.error("Class not found while attempting to find component type: " + className + " from properties with " +
                            "property name: {} and value: {}", entry.getKey(), entry.getValue());
                    throw new RuntimeException(e);
                }
                try {
                    component = applicationContext.getBean(c);
                } catch (NoSuchBeanDefinitionException e) {
                    logger.warn("Configurer bean: " + c.getName() + " was not found in the context. " +
                            "Trying to create a new one through autowire capable bean factory.");
                    component = applicationContext.getAutowireCapableBeanFactory().createBean(c);
                }

            } else if (entry.getValue().startsWith("bean:")) {
                component = applicationContext.getBean(entry.getValue().substring(5).trim());
            } else {
                throw new IllegalArgumentException("Unresolvable component for key:" + entry.getKey() + " and " +
                        "value:" + entry.getValue());
            }
            componentsMap.put(entry.getKey(), component);
        }
        return componentsMap;
    }
}
