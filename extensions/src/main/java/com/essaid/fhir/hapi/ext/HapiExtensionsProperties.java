package com.essaid.fhir.hapi.ext;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "com.essaid.fhir.hapi.extensions")
@Configuration
public class HapiExtensionsProperties {

    private List<String> providerClassNames;

    public List<String> getProviderClassNames() {
        return providerClassNames;
    }

    public void setProviderClassNames(List<String> providerClassNames) {
        this.providerClassNames = providerClassNames;
    }

}
