package com.essaid.fhir.hapi.ext.hold;

import org.springframework.context.annotation.Import;

@org.springframework.context.annotation.Configuration
@Import({StaticWebConfiguration.class, TimsUiConfiguration.class})
public class Configuration {
}
