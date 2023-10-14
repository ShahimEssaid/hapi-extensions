package com.essaid.fhir.hapiext.ext.hold;

import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

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
