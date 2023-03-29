package com.essaid.fhir.hapi.ext;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "com.essaid.fhir.hapi.extensions")
@Configuration
public class HapiExtensionsProperties {

    private final Map<String, Provider> providers = new HashMap<>();
    private String shutdownPassword = "password";

    public String getShutdownPassword() {
        return shutdownPassword;
    }

    public void setShutdownPassword(String shutdownPassword) {
        this.shutdownPassword = shutdownPassword;
    }

    public Map<String, Provider> getProviders() {
        return providers;
    }

    public static class Provider {
        private String className;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        @Override
        public String toString() {
            return "Provider{" + "className='" + className + '\'' + '}';
        }
    }
}
