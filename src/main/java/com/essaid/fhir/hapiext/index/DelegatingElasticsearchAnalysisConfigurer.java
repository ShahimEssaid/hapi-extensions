package com.essaid.fhir.hapiext.index;

import ca.uhn.fhir.jpa.search.HapiHSearchAnalysisConfigurers;
import com.essaid.fhir.hapiext.Properties;
import com.essaid.fhir.hapiext.util.HxUtils;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Lazy
public class DelegatingElasticsearchAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {

    private final Properties properties;
    private final ConfigurableApplicationContext applicationContext;

    public DelegatingElasticsearchAnalysisConfigurer(Properties properties,
                                                     ConfigurableApplicationContext applicationContext) {
        this.properties = properties;
        this.applicationContext = applicationContext;
    }

    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {
        Properties.IndexProperties indexProperties = properties.getIndex();
        Properties.IndexAnalysisProperties elasticAnalysisProperties = indexProperties.getElasticsearchAnalysis();
        if (elasticAnalysisProperties.isApplyDefault()) {
            new HapiHSearchAnalysisConfigurers.HapiElasticsearchAnalysisConfigurer().configure(context);
        }

        Map<String, ElasticsearchAnalysisConfigurer> configurersMap = HxUtils.resolveConfigurers(ElasticsearchAnalysisConfigurer.class, elasticAnalysisProperties.getConfigurers(), applicationContext);
        List<ElasticsearchAnalysisConfigurer> resolved = configurersMap.values().stream().filter(Objects::nonNull).filter(c -> c != this).collect(Collectors.toList());
        if (elasticAnalysisProperties.getConfigurers().isSort()) {
            AnnotationAwareOrderComparator.sort(resolved);

        }
        for (ElasticsearchAnalysisConfigurer configurer : resolved) {
            configurer.configure(context);
        }
    }


}
