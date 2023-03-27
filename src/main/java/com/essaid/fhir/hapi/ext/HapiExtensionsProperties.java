package com.essaid.fhir.hapi.ext;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "com.essaid.fhir.hapi.extensions")
@Configuration
public class HapiExtensionsProperties {

//    private List<String> providerClassNames = new ArrayList<>();

    public Map<String, Provider> getProviders() {
        return providers;
    }

    private final Map<String, Provider> providers = new HashMap<>();

//    public List<String> getProviderClassNames() {
//        return providerClassNames;
//    }
//
//    public void setProviderClassNames(List<String> providerClassNames) {
//        this.providerClassNames = providerClassNames;
//    }
//


    public static class Provider {
        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        private String className;

        @Override
        public String toString() {
            return "Provider{" +
                    "className='" + className + '\'' +
                    '}';
        }
    }
}
