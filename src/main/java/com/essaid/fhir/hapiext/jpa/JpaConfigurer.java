package com.essaid.fhir.hapiext.jpa;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public interface JpaConfigurer {
    void configure(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean);
}
