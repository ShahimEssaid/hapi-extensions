package com.essaid.fhir.hapi.ext.test.misc;

import ca.uhn.fhir.rest.server.RestfulServer;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.core.ResolvableType;

public class ResolvableTypeExamples {

    public static void main(String[] args) {
        ResolvableType resolvableType = ResolvableType.forClass(RestfulServer.class);
        resolvableType = ResolvableType.forClass(PayloadApplicationEvent.class);
        resolvableType = ResolvableType.forClassWithGenerics(PayloadApplicationEvent.class, RestfulServer.class);
        resolvableType = ResolvableType.forInstance(PayloadApplicationEvent.class);

        System.out.println(resolvableType);


    }
}
