package com.essaid.fhir.hapiext.ext.hold;

import ca.uhn.fhir.jpa.search.HapiHSearchAnalysisConfigurers;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class HapiExtIndexConfiguration {

  @Bean
  @Order(0)
  public LuceneAnalysisConfigurer hapiLuceneAnalysisConfigurer(){
    return new HapiHSearchAnalysisConfigurers.HapiLuceneAnalysisConfigurer();
  }

  @Bean
  @Order(0)
  public ElasticsearchAnalysisConfigurer hapiElasticsearchAnalysisConfigurer(){
    return new HapiHSearchAnalysisConfigurers.HapiElasticsearchAnalysisConfigurer();
  }

}
