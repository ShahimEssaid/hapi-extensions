package com.essaid.fhir.hapi.extensions.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(prefix = "hapi.fhir", name = "staticLocation")
public class StaticWebConfiguration implements WebMvcConfigurer {

    @Value("${hapi.fhir.staticLocation}")
    private String staticLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("================ STATIC LOCATION: " + staticLocation);
        registry.addResourceHandler("/static/**").addResourceLocations(staticLocation);
    }
}
