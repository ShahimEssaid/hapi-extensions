package com.essaid.fhir.hapi.ext.em;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public interface IEntityManagerFactoryBeanConfigurer {
    void configure(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean);
}
