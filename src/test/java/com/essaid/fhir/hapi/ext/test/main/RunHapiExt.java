package com.essaid.fhir.hapi.ext.test.main;

import ca.uhn.fhir.jpa.starter.Application;
import com.essaid.fhir.hapi.ext.HapiExtensionConfiguration;
import com.essaid.fhir.hapi.ext.component.ApplicationReadyReport;
import com.essaid.fhir.hapi.ext.test.example.EMFBConfigurerExample;
import com.essaid.fhir.hapi.ext.test.example.IndexMapperConfigurerExample;
import com.essaid.fhir.hapi.ext.test.example.LuceneAnalysisConfigurerExample;
import com.essaid.fhir.hapi.ext.test.example.RestfulServerConfigurerExample;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({Application.class,

        RestfulServerConfigurerExample.class,
        EMFBConfigurerExample.class,
IndexMapperConfigurerExample.class,
LuceneAnalysisConfigurerExample.class,
        HapiExtensionConfiguration.class,
ApplicationReadyReport.class})
public class RunHapiExt {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(RunHapiExt.class);

        ConfigurableApplicationContext context = app.run(args);

//        context.publishEvent(new RestfulServerCreatedEvent(RunHapiExt.class, context.getBean(RestfulServer.class)));

    }
}
