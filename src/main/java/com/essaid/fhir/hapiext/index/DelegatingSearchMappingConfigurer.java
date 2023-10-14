package com.essaid.fhir.hapiext.index;

import com.essaid.fhir.hapiext.Properties;
import com.essaid.fhir.hapiext.util.HxUtils;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Lazy
public class DelegatingSearchMappingConfigurer implements HibernateOrmSearchMappingConfigurer {

    private final Properties properties;
    private final ConfigurableApplicationContext applicationContext;

    public DelegatingSearchMappingConfigurer(Properties properties,
                                             ConfigurableApplicationContext applicationContext) {
        this.properties = properties;
        this.applicationContext = applicationContext;
    }

    @Override
    public void configure(HibernateOrmMappingConfigurationContext context) {
        Properties.IndexMappingProperties mappingProperties = properties.getIndex().getMapping();
        Map<String, HibernateOrmSearchMappingConfigurer> configurersMap = HxUtils.resolveConfigurers(HibernateOrmSearchMappingConfigurer.class, mappingProperties.getConfigurers(), applicationContext);
        List<HibernateOrmSearchMappingConfigurer> resolved = configurersMap.values().stream().filter(Objects::nonNull).filter(c -> c != this).collect(Collectors.toList());
        if (mappingProperties.getConfigurers().isSort()) {
            AnnotationAwareOrderComparator.sort(resolved);

        }
        for (HibernateOrmSearchMappingConfigurer configurer : resolved) {
            configurer.configure(context);
        }
    }
}
