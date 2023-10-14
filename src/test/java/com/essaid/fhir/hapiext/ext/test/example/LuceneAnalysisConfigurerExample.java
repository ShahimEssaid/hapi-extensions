package com.essaid.fhir.hapiext.ext.test.example;

import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

public class LuceneAnalysisConfigurerExample implements LuceneAnalysisConfigurer {
    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        System.out.println("Configuring LuceneAnalysisConfigurationContext");
    }
}
