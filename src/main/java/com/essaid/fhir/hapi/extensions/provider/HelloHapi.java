package com.essaid.fhir.hapi.extensions.provider;

import ca.uhn.fhir.rest.annotation.Operation;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class HelloHapi {
  
  @Operation(name = "hello-hapi", idempotent = true)
  public Basic helloHapi(){
    return new Basic().setCode(new CodeableConcept().addCoding(new Coding().setCode("hello-hapi")));
  }
}
