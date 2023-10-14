package com.essaid.fhir.hapiext.jpa;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.starter.common.StarterJpaConfig;
import com.essaid.fhir.hapiext.Properties;
import com.essaid.fhir.hapiext.index.DelegatingElasticsearchAnalysisConfigurer;
import com.essaid.fhir.hapiext.index.DelegatingLuceneAnalysisConfigurer;
import com.essaid.fhir.hapiext.index.DelegatingSearchMappingConfigurer;
import com.essaid.fhir.hapiext.util.HxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AutoConfiguration
@ConditionalOnProperty(prefix = Properties.PROPERTY_PREFIX, name = "jpa.enable", havingValue = "true")
@Import({Properties.class,
        DelegatingSearchMappingConfigurer.class,
        DelegatingLuceneAnalysisConfigurer.class,
        DelegatingElasticsearchAnalysisConfigurer.class})
public class JpaAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(JpaAutoConfiguration.class);

    private final StarterJpaConfig starterJpaConfig;
    private final Properties properties;
    private final ConfigurableApplicationContext applicationContext;

    public JpaAutoConfiguration(Properties properties,
                                StarterJpaConfig starterJpaConfig,
                                ConfigurableApplicationContext applicationContext) {
        this.properties = properties;
        this.starterJpaConfig = starterJpaConfig;
        this.applicationContext = applicationContext;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource myDataSource,
                                                                       ConfigurableListableBeanFactory myConfigurableListableBeanFactory,
                                                                       FhirContext theFhirContext) {

        LocalContainerEntityManagerFactoryBean emf = starterJpaConfig.entityManagerFactory(myDataSource, myConfigurableListableBeanFactory, theFhirContext);

        HxUtils.logMap("================  Upstream JPA Properties  =================", emf.getJpaPropertyMap(), logger, properties.getJpa().getLogLevel());

        if (properties.getJpa().isProcessProperties()) {
            emf.getJpaPropertyMap().putAll(properties.getJpa().getOverrideProperties());
        }

        HxUtils.logMap("================  JPA Properties after processing  =================", emf.getJpaPropertyMap(), logger, properties.getJpa().getLogLevel());

        List<JpaConfigurer> configurers = HxUtils.resolveConfigurers(JpaConfigurer.class, properties.getJpa().getConfigurers(), applicationContext)
                .values().stream().filter(Objects::nonNull).collect(Collectors.toList());
        AnnotationAwareOrderComparator.sort(configurers);
        configurers.forEach(c -> {
            logger.info("Applying JPA configurer: " + c.getClass().getName());
            c.configure(emf);
        });

        HxUtils.logMap("================  JPA Properties after configurers  =================", emf.getJpaPropertyMap(), logger, properties.getJpa().getLogLevel());

        return emf;
    }


}
