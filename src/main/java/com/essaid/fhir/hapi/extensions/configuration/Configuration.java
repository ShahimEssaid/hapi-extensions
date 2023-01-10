package com.essaid.fhir.hapi.extensions.configuration;

import org.springframework.context.annotation.Import;

@org.springframework.context.annotation.Configuration
@Import({StaticWebConfiguration.class, TimsUiConfiguration.class})
public class Configuration {
}
