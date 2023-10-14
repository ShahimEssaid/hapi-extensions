package com.essaid.fhir.hapiext.ext.hold;

import ca.uhn.fhir.rest.server.RestfulServer;
import com.essaid.fhir.hapiext.Properties;
import com.essaid.fhir.hapiext.server.ServerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Applies {@link ServerConfigurer} specified in configuration properties.
 */
@Component
public class RSPropertyConfigurer implements ServerConfigurer, ApplicationListener<HXBeanCreatedEvent<RestfulServer>> {

    private static final Logger logger = LoggerFactory.getLogger(RSPropertyConfigurer.class);

    final ApplicationContext applicationContext;
    final Properties properties;

    public RSPropertyConfigurer(ApplicationContext applicationContext,
                                Properties properties) {

        this.applicationContext = applicationContext;
        this.properties = properties;
    }

    @EventListener
    public void onApplicationEvent(HXBeanCreatedEvent<RestfulServer> server) {
        configure(server.getPayload());
    }

    @Override
    public void configure(RestfulServer restfulServer) {
        for (Map.Entry<String, String> entry : properties.getServer().getConfigurers().getMap().entrySet()) {

            ServerConfigurer configurer = null;
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (entry.getValue().startsWith("class:")) {
                String className = entry.getValue().substring(6).trim();
                try {
                    Class<?> c = cl.loadClass(className);
                    configurer = (ServerConfigurer) applicationContext.getBean(c);
                } catch (ClassNotFoundException e) {
                    logger.error("Class not found while attempting to load restful configurer from properties with " +
                            "property name: {} and value: {}", entry.getKey(), entry.getValue());
                    throw new RuntimeException(e);
                }
            } else if (entry.getValue().startsWith("bean:")) {
                configurer = applicationContext.getBean(entry.getValue().substring(5).trim(),
                        ServerConfigurer.class);
            } else {
                throw new IllegalArgumentException("Unparsable configurer value for key:" + entry.getKey() + " and " +
                        "value:" + entry.getValue());
            }

            configurer.configure(restfulServer);

        }

    }

}
