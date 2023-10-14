package com.essaid.fhir.hapiext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.essaid.fhir.hapiext.Properties.PROPERTY_PREFIX;

@Configuration
@ConfigurationProperties(prefix = PROPERTY_PREFIX)
public class Properties {

    public static final String PROPERTY_PREFIX = "hapiext";

    private final JpaProperties jpa = new JpaProperties();
    private final ServerProperties server = new ServerProperties();
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
        return gson.toJson(this, Properties.class);
    }

    public ServerProperties getServer() {
        return server;
    }


    public JpaProperties getJpa() {
        return jpa;
    }


    public static class ServerProperties {
        private final Configurers configurers = new Configurers();


        public Configurers getConfigurers() {
            return configurers;
        }

    }

    public static class IndexProperties {
        private final IndexAnalysisProperties luceneAnalysis = new IndexAnalysisProperties();
        private final IndexAnalysisProperties elasticsearchAnalysis = new IndexAnalysisProperties();
        private final IndexMappingProperties mapping = new IndexMappingProperties();

        public IndexAnalysisProperties getElasticsearchAnalysis() {
            return elasticsearchAnalysis;
        }

        public IndexAnalysisProperties getLuceneAnalysis() {
            return luceneAnalysis;
        }

        public IndexMappingProperties getMapping() {
            return mapping;
        }

    }

    public static class IndexAnalysisProperties {
        private final Configurers configurers = new Configurers();
        private boolean applyDefault;

        public boolean isApplyDefault() {
            return applyDefault;
        }

        public void setApplyDefault(boolean applyDefault) {
            this.applyDefault = applyDefault;
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

    public static class JpaProperties {

        private final Configurers configurers = new Configurers();
        private final Map<String, String> overrideProperties = new HashMap<>();
        private boolean processProperties;
        private String logLevel;
        private boolean consoleLog;

        public String getLogLevel() {
            return logLevel;
        }

        public void setLogLevel(String logLevel) {
            this.logLevel = logLevel;
        }

        public boolean isProcessProperties() {
            return processProperties;
        }

        public void setProcessProperties(boolean processProperties) {
            this.processProperties = processProperties;
        }


        public Map<String, String> getOverrideProperties() {
            return overrideProperties;
        }

        public Configurers getConfigurers() {
            return configurers;
        }

    }

    public static class Configurers {
        public static final String TYPE_MAP = "map";
        public static final String TYPE_CONTEXT = "context";
        private final Map<String, String> map = new HashMap<>();
        boolean sort;
        private String type = TYPE_CONTEXT;

        public boolean isSort() {
            return sort;
        }

        public void setSort(boolean sort) {
            this.sort = sort;
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
