package com.essaid.fhir.hapi.ext.x;

import ca.uhn.fhir.rest.annotation.Operation;
import org.hibernate.search.engine.backend.metamodel.IndexDescriptor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.entity.SearchIndexedEntity;
import org.hibernate.search.mapper.orm.mapping.SearchMapping;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Collection;

@Component
@Profile("x")
public class XPrintIndexMappingProvider {
    private final EntityManager em;

    public XPrintIndexMappingProvider(EntityManager em) {
        Ordered ordered;
        this.em = em;
        System.out.println("============ EM:" + em);
    }


}
