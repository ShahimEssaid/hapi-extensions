package com.essaid.fhir.hapiext.jpa;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class NullJpaConfigurer implements JpaConfigurer {
    @Override
    public void configure(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {

    }
}
