package com.essaid.fhir.hapiext.server;

import ca.uhn.fhir.batch2.jobs.export.BulkDataExportProvider;
import ca.uhn.fhir.batch2.jobs.imprt.BulkDataImportProvider;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexProvider;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.binary.interceptor.BinaryStorageInterceptor;
import ca.uhn.fhir.jpa.binary.provider.BinaryAccessProvider;
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
import ca.uhn.fhir.jpa.starter.AppProperties;
import ca.uhn.fhir.jpa.starter.common.StarterJpaConfig;
import ca.uhn.fhir.mdm.provider.MdmProviderLoader;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.validation.IValidatorModule;
import com.essaid.fhir.hapiext.Properties;
import com.essaid.fhir.hapiext.util.HxUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AutoConfiguration
@Import({Properties.class})
public class ServerAutoConfiguration {

    private final Properties properties;
    private final ConfigurableApplicationContext applicationContext;
    private final StarterJpaConfig starterJpaConfig;

    public ServerAutoConfiguration(Properties properties,
                                   ConfigurableApplicationContext applicationContext,
                                   StarterJpaConfig starterJpaConfig) {
        this.properties = properties;
        this.applicationContext = applicationContext;
        this.starterJpaConfig = starterJpaConfig;
    }

    @Bean
    public RestfulServer restfulServer(
            IFhirSystemDao<?, ?> fhirSystemDao,
            AppProperties appProperties,
            DaoRegistry daoRegistry,
            Optional<MdmProviderLoader> mdmProviderProvider,
            IJpaSystemProvider jpaSystemProvider,
            ResourceProviderFactory resourceProviderFactory,
            JpaStorageSettings jpaStorageSettings,

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
            IValidatorModule validatorModule,
            Optional<GraphQLProvider> graphQLProvider,
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
                mdmProviderProvider, jpaSystemProvider, resourceProviderFactory, jpaStorageSettings, searchParamRegistry,
                theValidationSupport, databaseBackedPagingProvider, loggingInterceptor, terminologyUploaderProvider,
                subscriptionTriggeringProvider, corsInterceptor, interceptorBroadcaster, binaryAccessProvider,
                binaryStorageInterceptor, validatorModule, graphQLProvider, bulkDataExportProvider,
                bulkDataImportProvider, theValueSetOperationProvider, reindexProvider, partitionManagementProvider,
                repositoryValidatingInterceptor, packageInstallerSvc, theThreadSafeResourceDeleterSvc, appContext,
                theIpsOperationProvider);

        Properties.ServerProperties serverProperties = properties.getServer();
        List<ServerConfigurer> configurers = HxUtils.resolveConfigurers(ServerConfigurer.class, serverProperties.getConfigurers(), applicationContext)
                .values().stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (serverProperties.getConfigurers().isSort()) {
            AnnotationAwareOrderComparator.sort(configurers);
        }
        configurers.forEach(c -> c.configure(restfulServer));
        return restfulServer;
    }
}
