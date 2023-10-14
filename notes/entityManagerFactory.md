HAPI uses a `LocalContainerEntityManagerFactoryBean` to configure JPA. It is created in `ca.uhn.fhir.jpa.starter.common.StarterJpaConfig.entityManagerFactory`. Instead of relying on Spring Boot AutoConfiguration, HAPI manually instantiates it and manages the property settings. The following is a walkthrough of how this bean is set up in HAPI v6.8.0, and how the Starter configures the properties.

* HAPI `HapiFhirLocalContainerEntityManagerFactoryBean` extends `LocalContainerEntityManagerFactoryBean`
  * In its `getJpaPropertyMap` it **defaults** a few properties. 
  * See `entityManagerFactory.yml` in this folder for the details.
* HAPI `HapiFhirLocalContainerEntityManagerFactoryBean` is instantiated in `ca.uhn.fhir.jpa.config.util.HapiEntityManagerFactoryUtil`
  * The dialect is set to an instance of `ca.uhn.fhir.jpa.config.HapiFhirHibernateJpaDialect`, which is constructed with an instance of `ca.uhn.fhir.i18n.HapiLocalizer` from the `FhirContext`.
  * The packages to scan are set to `ca.uhn.fhir.jpa.model.entity` and `ca.uhn.fhir.jpa.entity`
  * The persistance provider is set to `org.hibernate.jpa.HibernatePersistenceProvider`
* In the `ca.uhn.fhir.jpa.starter.common.StarterJpaConfig.entityManagerFactory` bean factory method:
  * The persistence unit name is set to `HAPI_PU`.
  * The datasource is set from the container.
  * The properties are set with the results of `ca.uhn.fhir.jpa.starter.util.EnvironmentHelper.getHibernateProperties`
* In `ca.uhn.fhir.jpa.starter.util.EnvironmentHelper.getHibernateProperties` the properties are built up by:
  * Copying all the properties under `spring.jpa.properties`
  * Defaulting values.  See `entityManagerFactory.yml` in this folder for the details.
    * It instantiates a throwaway instance of `HapiFhirLocalContainerEntityManagerFactoryBean` just to get the default properties from that class and use them as defaults if absent.
    * 



