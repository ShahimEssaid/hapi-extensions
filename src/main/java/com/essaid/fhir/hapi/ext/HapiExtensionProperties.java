package com.essaid.fhir.hapi.ext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "com.essaid.hapiext")
public class HapiExtensionProperties {

    private final RestfulServerProperties restfulServer = new RestfulServerProperties();
    private final EntityManagerFactoryProperties entityManagerFactory = new EntityManagerFactoryProperties();
    private final IndexProperties index = new IndexProperties();
    private final Map<String, Map<String, Object>> providersConfiguration = new HashMap<>();

    public Map<String, Map<String, Object>> getProvidersConfiguration() {
        return providersConfiguration;
    }

    public IndexProperties getIndex() {
        return index;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this, HapiExtensionProperties.class);
    }

    public RestfulServerProperties getRestfulServer() {
        return restfulServer;
    }


    public EntityManagerFactoryProperties getEntityManagerFactory() {
        return entityManagerFactory;
    }


    public static class RestfulServerProperties {
        private final Configurers configurers = new Configurers();


        public Configurers getConfigurers() {
            return configurers;
        }

    }

    public static class IndexProperties {
        private final IndexAnalysisProperties analysis = new IndexAnalysisProperties();
        private final IndexMappingProperties mapping = new IndexMappingProperties();

        public IndexAnalysisProperties getAnalysis() {
            return analysis;
        }

        public IndexMappingProperties getMapping() {
            return mapping;
        }

    }

    public static class IndexAnalysisProperties {
        private final Configurers configurers = new Configurers();
        private boolean applyDefaults;

        public boolean isApplyDefaults() {
            return applyDefaults;
        }

        public void setApplyDefaults(boolean applyDefaults) {
            this.applyDefaults = applyDefaults;
        }

        public Configurers getConfigurers() {
            return configurers;
        }

    }

    public static class IndexMappingProperties {
        private final Configurers configurers = new Configurers();

        public Configurers getConfigurers() {
            return configurers;
        }

    }

    public static class EntityManagerFactoryProperties {

        private final Configurers configurers = new Configurers();
        private final Map<String, String> defaultProperties = new HashMap<>();
        private final Map<String, String> overrideProperties = new HashMap<>();
        private boolean processProperties;

        public boolean isProcessProperties() {
            return processProperties;
        }

        public void setProcessProperties(boolean processProperties) {
            this.processProperties = processProperties;
        }

        public Map<String, String> getDefaultProperties() {
            return defaultProperties;
        }

        public Map<String, String> getOverrideProperties() {
            return overrideProperties;
        }

        public Configurers getConfigurers() {
            return configurers;
        }

    }

    public static class Configurers {
        static final String TYPE_MAP = "map";
        static final String TYPE_CONTEXT = "context";
        private final Map<String, String> map = new HashMap<>();
        private boolean enabled;
        private String type = TYPE_CONTEXT;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Map<String, String> getMap() {
            return map;
        }
    }

}
