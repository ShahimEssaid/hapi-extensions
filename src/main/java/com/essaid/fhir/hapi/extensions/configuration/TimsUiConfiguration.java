package com.essaid.fhir.hapi.extensions.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(prefix = "tims", name = "uiLocation")
public class TimsUiConfiguration implements WebMvcConfigurer {

    @Value("${tims.uiLocation}")
    private String timsUiLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("================ TIMS UI LOCATION: " + timsUiLocation);
        registry.addResourceHandler("/timsui/**").addResourceLocations(timsUiLocation);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/timsui/", "/timsui/index.html");
        registry.addRedirectViewController("/timsui", "/timsui/index.html");
    }
}
