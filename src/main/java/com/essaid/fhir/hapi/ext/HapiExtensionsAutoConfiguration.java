package com.essaid.fhir.hapi.ext;

import ca.uhn.fhir.jpa.starter.Application;
import com.essaid.fhir.hapi.ext.component.ComponentConfiguration;
import com.essaid.fhir.hapi.ext.processor.RestfulServerPostProcessor;
import com.essaid.fhir.hapi.ext.provider.HelloHapi;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({HapiExtensionsProperties.class, RestfulServerPostProcessor.class, com.essaid.fhir.hapi.ext.configuration.Configuration.class,
        ComponentConfiguration.class})
@ComponentScan(basePackageClasses = {HelloHapi.class})
public class HapiExtensionsAutoConfiguration {

  public HapiExtensionsAutoConfiguration() {
    System.out.println("==============  HapiExtensionsConfiguration  ");
  }


}
