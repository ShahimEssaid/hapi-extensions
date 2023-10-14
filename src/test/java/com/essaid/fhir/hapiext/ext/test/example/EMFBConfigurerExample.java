package com.essaid.fhir.hapiext.ext.test.example;

import com.essaid.fhir.hapiext.jpa.JpaConfigurer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EMFBConfigurerExample implements JpaConfigurer {
    @Override
    public void configure(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
//        entityManagerFactoryBean.getJpaPropertyMap().put("hibernate.search.backend.analysis.configurer", "TEST");
        List<String> keys = new ArrayList<>(entityManagerFactoryBean.getJpaPropertyMap().keySet());
        keys.stream().sorted().forEach(key -> System.out.println("Key:" + key + " value:" + entityManagerFactoryBean.getJpaPropertyMap().get(key)));
        System.out.println(entityManagerFactoryBean);
    }
}
