package com.essaid.fhir.hapi.ext.component;

import org.hibernate.search.engine.backend.index.IndexManager;
import org.hibernate.search.engine.backend.metamodel.IndexDescriptor;
import org.hibernate.search.engine.backend.metamodel.IndexFieldDescriptor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.entity.SearchIndexedEntity;
import org.hibernate.search.mapper.orm.mapping.SearchMapping;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.util.Collection;

@Component
public class ApplicationReadyReport {

    private final ApplicationContext context;

    public ApplicationReadyReport(ApplicationContext context) {
        this.context = context;
    }

    @EventListener
    public void ready(ApplicationReadyEvent event){
        EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);
        SearchMapping mapping = Search.mapping(entityManagerFactory);
        Collection<? extends SearchIndexedEntity<?>> searchIndexedEntities = mapping.allIndexedEntities();
        for (SearchIndexedEntity<?> e : searchIndexedEntities) {
            IndexManager indexManager = e.indexManager();
            IndexDescriptor descriptor = indexManager.descriptor();
            Collection<IndexFieldDescriptor> staticFields = descriptor.staticFields();
            System.out.println("\t" + descriptor);
            for (IndexFieldDescriptor fieldDescriptor : staticFields) {
                System.out.println("\t\t" + fieldDescriptor);
            }

        }
    }
}
