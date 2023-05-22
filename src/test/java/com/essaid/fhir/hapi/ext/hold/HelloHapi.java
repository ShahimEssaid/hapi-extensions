package com.essaid.fhir.hapi.ext.hold;

import ca.uhn.fhir.rest.annotation.Operation;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.Set;

@Component
@Lazy
public class HelloHapi {

  @Autowired
  EntityManager entityManager;
  
  @Operation(name = "hello-hapi", idempotent = true)
  public Basic helloHapi(){
    System.out.println(entityManager);
    Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
    return new Basic().setCode(new CodeableConcept().addCoding(new Coding().setCode("hello-hapi")));
  }
}
