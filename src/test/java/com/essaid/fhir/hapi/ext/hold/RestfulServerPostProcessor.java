package com.essaid.fhir.hapi.ext.hold;

import ca.uhn.fhir.rest.server.RestfulServer;
import com.essaid.fhir.hapi.ext.HapiExtensionProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RestfulServerPostProcessor implements BeanPostProcessor, ApplicationContextAware {

  private final Environment environment;
  HapiExtensionProperties properties;
  private ApplicationContext context;

  public RestfulServerPostProcessor(HapiExtensionProperties properties, Environment environment) {
    this.properties = properties;
    this.environment = environment;
  }

  @Override
  public Object postProcessAfterInitialization(@NotNull Object bean, String beanName) throws BeansException {

    if (beanName.equals("restfulServer")) {
      RestfulServer hapiServer = (RestfulServer) bean;
//      properties.getProviders().forEach((providerKey, providerConfig) -> {
//        try {
//          Object provider = this.context.getBean(RestfulServerPostProcessor.class.getClassLoader().loadClass(providerConfig.getClassName()));
//          if (provider != null) {
//            System.out.println("=============== Loading provider:: " + providerConfig);
//            hapiServer.registerProvider(provider);
//          } else {
//            System.out.println("=============== Provider not found: " + providerConfig);
//          }
//
//
//        } catch (ClassNotFoundException e) {
//          throw new RuntimeException(e);
//        }
//      });
//            properties.getProviderClassNames().forEach(s -> {
//                try {
//                    hapiServer.registerProvider(this.context.getBean(RestfulServerPostProcessor.class.getClassLoader().loadClass(s)));
//                    System.out.println("============= Extensions provider: " + s);
//
//                } catch (ClassNotFoundException e) {
//                    throw new RuntimeException(e);
//                }
//
//                if (environment.acceptsProfiles(activeProfiles -> {
//                    return activeProfiles.test("x");
//                })) {
//                  hapiServer.registerProvider(this.context.getBean(XPrintIndexMappingProvider.class));
//                }
//
//            });


      System.out.println("============= Extensions providers done");
    }

    return bean;
  }

  @Override
  public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }
}