package com.essaid.fhir.hapiext.index;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;
import org.springframework.stereotype.Component;

@Component
public class NullIndexAnalysisConfigurer implements LuceneAnalysisConfigurer, ElasticsearchAnalysisConfigurer {


    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {

    }

    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {

    }
}
