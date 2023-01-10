package com.essaid.fhir.hapi.extensions;

import com.essaid.fhir.hapi.extensions.provider.HelloHapi;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({HapiExtensionsProperties.class, RestfulServerPostProcessor.class, com.essaid.fhir.hapi.extensions.configuration.Configuration.class})
@ComponentScan(basePackageClasses = {HelloHapi.class})

public class HapiExtensionsConfiguration {


}
