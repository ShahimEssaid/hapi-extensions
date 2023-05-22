package com.essaid.fhir.hapi.ext;

import ca.uhn.fhir.batch2.jobs.imprt.BulkDataImportProvider;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexProvider;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.binary.interceptor.BinaryStorageInterceptor;
import ca.uhn.fhir.jpa.binary.provider.BinaryAccessProvider;
import ca.uhn.fhir.jpa.bulk.export.provider.BulkDataExportProvider;
import ca.uhn.fhir.jpa.delete.ThreadSafeResourceDeleterSvc;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingInterceptor;
import ca.uhn.fhir.jpa.ips.provider.IpsOperationProvider;
import ca.uhn.fhir.jpa.packages.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.partition.PartitionManagementProvider;
import ca.uhn.fhir.jpa.provider.IJpaSystemProvider;
import ca.uhn.fhir.jpa.provider.SubscriptionTriggeringProvider;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.HapiHSearchAnalysisConfigurers;
import ca.uhn.fhir.jpa.starter.AppProperties;
import ca.uhn.fhir.jpa.starter.common.StarterJpaConfig;
import ca.uhn.fhir.mdm.provider.MdmProviderLoader;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.validation.IValidatorModule;
import com.essaid.fhir.hapi.ext.em.IEntityManagerFactoryBeanConfigurer;
import com.essaid.fhir.hapi.ext.server.IRestfulServerConfigurer;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmMappingConfigurationContext;
import org.hibernate.search.mapper.orm.mapping.HibernateOrmSearchMappingConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@Import({HapiExtensionProperties.class})
public class HapiExtensionConfiguration implements HibernateOrmSearchMappingConfigurer, LuceneAnalysisConfigurer, ElasticsearchAnalysisConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(HapiExtensionConfiguration.class);

    final ApplicationContext applicationContext;
    final HapiExtensionProperties hapiExtensionProperties;
    final StarterJpaConfig starterJpaConfig;

    HapiExtensionConfiguration(ApplicationContext applicationContext, HapiExtensionProperties hapiExtensionProperties,
                               StarterJpaConfig starterJpaConfig) {
        this.applicationContext = applicationContext;
        this.hapiExtensionProperties = hapiExtensionProperties;
        this.starterJpaConfig = starterJpaConfig;
    }


    @Bean
    public RestfulServer restfulServer(
            IFhirSystemDao<?, ?> fhirSystemDao,
            AppProperties appProperties, DaoRegistry daoRegistry,
            Optional<MdmProviderLoader> mdmProviderProvider,
            IJpaSystemProvider jpaSystemProvider,
            ResourceProviderFactory resourceProviderFactory, DaoConfig daoConfig,
            ISearchParamRegistry searchParamRegistry,
            IValidationSupport theValidationSupport,
            DatabaseBackedPagingProvider databaseBackedPagingProvider,
            LoggingInterceptor loggingInterceptor,
            Optional<TerminologyUploaderProvider> terminologyUploaderProvider,
            Optional<SubscriptionTriggeringProvider> subscriptionTriggeringProvider,
            Optional<CorsInterceptor> corsInterceptor,
            IInterceptorBroadcaster interceptorBroadcaster,
            Optional<BinaryAccessProvider> binaryAccessProvider,
            BinaryStorageInterceptor binaryStorageInterceptor,
            IValidatorModule validatorModule, Optional<GraphQLProvider> graphQLProvider,
            BulkDataExportProvider bulkDataExportProvider,
            BulkDataImportProvider bulkDataImportProvider,
            ValueSetOperationProvider theValueSetOperationProvider,
            ReindexProvider reindexProvider,
            PartitionManagementProvider partitionManagementProvider,
            Optional<RepositoryValidatingInterceptor> repositoryValidatingInterceptor,
            IPackageInstallerSvc packageInstallerSvc,
            ThreadSafeResourceDeleterSvc theThreadSafeResourceDeleterSvc,
            ApplicationContext appContext,
            Optional<IpsOperationProvider> theIpsOperationProvider) {

        RestfulServer restfulServer = starterJpaConfig.restfulServer(fhirSystemDao, appProperties, daoRegistry,
                mdmProviderProvider, jpaSystemProvider, resourceProviderFactory, daoConfig, searchParamRegistry,
                theValidationSupport, databaseBackedPagingProvider, loggingInterceptor, terminologyUploaderProvider,
                subscriptionTriggeringProvider, corsInterceptor, interceptorBroadcaster, binaryAccessProvider,
                binaryStorageInterceptor, validatorModule, graphQLProvider, bulkDataExportProvider,
                bulkDataImportProvider, theValueSetOperationProvider, reindexProvider, partitionManagementProvider,
                repositoryValidatingInterceptor, packageInstallerSvc, theThreadSafeResourceDeleterSvc, appContext,
                theIpsOperationProvider);

        List<IRestfulServerConfigurer> configurers = resolveConfigurers(IRestfulServerConfigurer.class, hapiExtensionProperties.getRestfulServer().getConfigurers())
                .values().stream().filter(Objects::nonNull).collect(Collectors.toList());
        AnnotationAwareOrderComparator.sort(configurers);
        configurers.forEach(c -> c.configure(restfulServer));
        return restfulServer;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource myDataSource, ConfigurableListableBeanFactory myConfigurableListableBeanFactory, FhirContext theFhirContext) {

        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = starterJpaConfig.entityManagerFactory(myDataSource, myConfigurableListableBeanFactory, theFhirContext);

        if (hapiExtensionProperties.getEntityManagerFactory().isProcessProperties()) {
            updateProperties(localContainerEntityManagerFactoryBean.getJpaPropertyMap(), hapiExtensionProperties.getEntityManagerFactory().getDefaultProperties(),
                    hapiExtensionProperties.getEntityManagerFactory().getOverrideProperties());
        }

        List<IEntityManagerFactoryBeanConfigurer> configurers = resolveConfigurers(IEntityManagerFactoryBeanConfigurer.class, hapiExtensionProperties.getEntityManagerFactory().getConfigurers())
                .values().stream().filter(Objects::nonNull).collect(Collectors.toList());
        AnnotationAwareOrderComparator.sort(configurers);
        configurers.forEach(c -> c.configure(localContainerEntityManagerFactoryBean));
        return localContainerEntityManagerFactoryBean;
    }

    @Override
    public void configure(HibernateOrmMappingConfigurationContext context) {
        HapiExtensionProperties.IndexMappingProperties mapping = hapiExtensionProperties.getIndex().getMapping();
        Map<String, HibernateOrmSearchMappingConfigurer> resolved = resolveConfigurers(HibernateOrmSearchMappingConfigurer.class, mapping.getConfigurers());
        List<HibernateOrmSearchMappingConfigurer> resolvedList = resolved.values().stream().filter(Objects::nonNull).filter(c -> c != this).collect(Collectors.toList());
        AnnotationAwareOrderComparator.sort(resolvedList);
        resolvedList.forEach( c -> {
            c.configure(context);
        });
    }

    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {
        HapiExtensionProperties.IndexAnalysisProperties analysis = hapiExtensionProperties.getIndex().getAnalysis();
        if (analysis.isApplyDefaults()) {
            new HapiHSearchAnalysisConfigurers.HapiElasticsearchAnalysisConfigurer().configure(context);
        }
        Map<String, ElasticsearchAnalysisConfigurer> resolved = resolveConfigurers(ElasticsearchAnalysisConfigurer.class, analysis.getConfigurers());
        List<ElasticsearchAnalysisConfigurer> resolvedList = resolved.values().stream().filter(Objects::nonNull).filter(c -> c != this).collect(Collectors.toList());
        AnnotationAwareOrderComparator.sort(resolvedList);
        resolvedList.forEach( c -> {
            c.configure(context);
        });
    }

    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        HapiExtensionProperties.IndexAnalysisProperties analysis = hapiExtensionProperties.getIndex().getAnalysis();
        if (analysis.isApplyDefaults()) {
            new HapiHSearchAnalysisConfigurers.HapiLuceneAnalysisConfigurer().configure(context);
        }

        Map<String, LuceneAnalysisConfigurer> resolved = resolveConfigurers(LuceneAnalysisConfigurer.class, analysis.getConfigurers());
        List<LuceneAnalysisConfigurer> resolvedList = resolved.values().stream().filter(Objects::nonNull).filter(c -> c != this).collect(Collectors.toList());
        AnnotationAwareOrderComparator.sort(resolvedList);
        resolvedList.forEach( c -> {
            c.configure(context);
        });
    }

    private <T> Map<String, T> resolveConfigurers(Class<T> configurerClass, HapiExtensionProperties.Configurers configurers) {

        Map<String, T> resolved = new HashMap<>();
        if (configurers.isEnabled()) {
            if (configurers.getType().equals(HapiExtensionProperties.Configurers.TYPE_MAP)) {
                resolved.putAll(resolveComponentsMap(configurers.getMap()).entrySet().stream().filter(e -> {
                            if (configurerClass.isInstance(e.getValue())) {
                                return true;
                            } else {
                                // todo: log
                                return false;
                            }
                        })
                        .collect(Collectors.toMap(e -> e.getKey(), e -> (T) e.getValue())));

            } else if (configurers.getType().equals(HapiExtensionProperties.Configurers.TYPE_CONTEXT)) {
                resolved = applicationContext.getBeansOfType(configurerClass);
            } else {
                throw new IllegalArgumentException(configurerClass.getName() + ": " + configurers.toString());
            }
        }
        return resolved;
    }

    private Map<String, Object> resolveComponentsMap(Map<String, String> componentMap) {
//        List<T> components = new ArrayList<>();
        Map<String, Object> componentsMap = new HashMap<>();

        for (Map.Entry<String, String> entry : componentMap.entrySet()) {
            if (entry.getValue().isEmpty()) {
                componentsMap.put(entry.getKey(), null);
                continue;
            }
            ;
            Object component = null;
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (entry.getValue().startsWith("class:")) {
                String className = entry.getValue().substring(6).trim();
                try {
                    Class<?> c = cl.loadClass(className);
                    component = applicationContext.getBean(c);
                } catch (ClassNotFoundException e) {
                    logger.error("Class not found while attempting to find component type: " + className + " from properties with " +
                            "property name: {} and value: {}", entry.getKey(), entry.getValue());
                    throw new RuntimeException(e);
                }
            } else if (entry.getValue().startsWith("bean:")) {
                component = applicationContext.getBean(entry.getValue().substring(5).trim());
            } else {
                throw new IllegalArgumentException("Unresolvable component for key:" + entry.getKey() + " and " +
                        "value:" + entry.getValue());
            }
            componentsMap.put(entry.getKey(), component);
        }
        return componentsMap;
    }

    private void updateProperties(Map<String, Object> properties, Map<String, String> defaultProperties, Map<String, String> overrideProperties) {

        for (Map.Entry<String, String> entry : defaultProperties.entrySet()) {
            if (entry.getValue().isEmpty()) continue;
            properties.putIfAbsent(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, String> entry : overrideProperties.entrySet()) {
            if (entry.getValue().isEmpty()) {
                properties.remove(entry.getKey());
                continue;
            }
            properties.put(entry.getKey(), entry.getValue());
        }
    }
}
