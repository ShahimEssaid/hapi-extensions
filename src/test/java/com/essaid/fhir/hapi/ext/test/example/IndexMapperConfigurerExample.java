package com.essaid.fhir.hapi.ext.test.example;

import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;

public class IndexMapperConfigurerExample implements HibernateOrmSearchMappingConfigurer {
    @Override
    public void configure(HibernateOrmMappingConfigurationContext context) {
        System.out.println("Configuring HibernateOrmMappingConfigurationContext");
    }
}
