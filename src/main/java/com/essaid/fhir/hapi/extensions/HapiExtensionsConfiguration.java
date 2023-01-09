package com.essaid.fhir.hapi.extensions;

import com.essaid.fhir.hapi.extensions.provider.HelloHapi;
import com.essaid.fhir.hapi.extensions.web.StaticWebConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({HapiExtensionsProperties.class, RestfulServerPostProcessor.class, StaticWebConfiguration.class})
@ComponentScan(basePackageClasses = HelloHapi.class)

public class HapiExtensionsConfiguration {


}
