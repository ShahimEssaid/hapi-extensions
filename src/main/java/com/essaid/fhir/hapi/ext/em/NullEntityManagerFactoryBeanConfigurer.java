package com.essaid.fhir.hapi.ext.em;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class NullEntityManagerFactoryBeanConfigurer implements IEntityManagerFactoryBeanConfigurer {
    @Override
    public void configure(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {

    }
}
