package com.essaid.fhir.hapiext.index;

import ca.uhn.fhir.jpa.search.HapiHSearchAnalysisConfigurers;
import com.essaid.fhir.hapiext.Properties;
import com.essaid.fhir.hapiext.util.HxUtils;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;
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
public class DelegatingLuceneAnalysisConfigurer implements LuceneAnalysisConfigurer {

    private final Properties properties;
    private final ConfigurableApplicationContext applicationContext;

    public DelegatingLuceneAnalysisConfigurer(Properties properties,
                                              ConfigurableApplicationContext applicationContext) {
        this.properties = properties;
        this.applicationContext = applicationContext;
    }

    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        Properties.IndexProperties indexProperties = properties.getIndex();
        Properties.IndexAnalysisProperties luceneAnalysisProperties = indexProperties.getLuceneAnalysis();
        if (luceneAnalysisProperties.isApplyDefault()) {
            new HapiHSearchAnalysisConfigurers.HapiLuceneAnalysisConfigurer().configure(context);
        }
        Map<String, LuceneAnalysisConfigurer> luceneAnalysisConfigurerMap = HxUtils.resolveConfigurers(LuceneAnalysisConfigurer.class, luceneAnalysisProperties.getConfigurers(), applicationContext);
        List<LuceneAnalysisConfigurer> resolved = luceneAnalysisConfigurerMap.values().stream().filter(Objects::nonNull).filter(c -> c != this).collect(Collectors.toList());
        if (luceneAnalysisProperties.getConfigurers().isSort()) {
            AnnotationAwareOrderComparator.sort(resolved);

        }
        for (LuceneAnalysisConfigurer configurer : resolved) {
            configurer.configure(context);
        }
    }
}
