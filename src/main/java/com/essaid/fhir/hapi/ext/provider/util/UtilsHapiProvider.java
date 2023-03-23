package com.essaid.fhir.hapi.ext.provider.util;

import ca.uhn.fhir.rest.annotation.Operation;
import org.hibernate.search.engine.backend.index.IndexManager;
import org.hibernate.search.engine.backend.metamodel.IndexCompositeElementDescriptor;
import org.hibernate.search.engine.backend.metamodel.IndexDescriptor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.entity.SearchIndexedEntity;
import org.hibernate.search.mapper.orm.mapping.SearchMapping;
import org.hibernate.search.mapper.orm.schema.management.SearchSchemaManager;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collection;

@Component
@Lazy
@Transactional
public class UtilsHapiProvider {
    private final EntityManager entityManager;

    public UtilsHapiProvider(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Operation(name = "ext-util-index-mapping", idempotent = true)
    public Basic postProcessAfterInitialization() throws BeansException {
        SearchMapping mapping = Search.mapping(entityManager.getEntityManagerFactory());
        SearchSession session = Search.session(entityManager);
        SearchSchemaManager searchSchemaManager = session.schemaManager();

        Collection<? extends SearchIndexedEntity<?>> searchIndexedEntities = mapping.allIndexedEntities();

        for (SearchIndexedEntity<?> indexedEntity : searchIndexedEntities) {

            IndexManager indexManager = indexedEntity.indexManager();
            IndexDescriptor descriptor = indexManager.descriptor();
            IndexCompositeElementDescriptor root = descriptor.root();
            StringBuilder sb = new StringBuilder(descriptor.hibernateSearchName());

            System.out.println("========= INDEX info:\n" + sb.toString());
        }
        return new Basic().setCode(new CodeableConcept().addCoding(new Coding().setCode("xPrintIndexMapping")));
    }
}
