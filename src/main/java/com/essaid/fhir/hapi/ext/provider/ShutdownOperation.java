package com.essaid.fhir.hapi.ext.provider;

import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import com.essaid.fhir.hapi.ext.HapiExtensionsProperties;
import com.essaid.fhir.hapi.ext.component.HapiExitManager;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.stereotype.Component;

@Component
public class ShutdownOperation {

    private final HapiExitManager exitManager;
    private final HapiExtensionsProperties extensionsProperties;

    public ShutdownOperation(HapiExtensionsProperties extensionsProperties, HapiExitManager exitManager) {
        this.exitManager = exitManager;
        this.extensionsProperties = extensionsProperties;
    }

    @Operation(name = "shutdown", idempotent = true)
    public Parameters shutdown(@OperationParam(name = "exit-code", typeName = "integer", min = 1, max = 1) IPrimitiveType<Integer> exitCode,
                                    @OperationParam(name = "password", typeName = "string", min = 1, max = 1) IPrimitiveType<String> password,
                               @OperationParam(name = "exit-delay", typeName = "integer", min = 1, max = 1) IPrimitiveType<Integer> exitDelay) {
        Parameters parameters = new Parameters();
        if (password == null || !password.getValue().equals(extensionsProperties.getShutdownPassword())) {
            parameters.addParameter().setName("Password").setValue(new StringType("Not set or not matching: " + (password != null ? password.getValue() : "Null")));
        } else {
            parameters.addParameter().setName("Shutting down").setValue(new StringType("True"));
            this.exitManager.setHapiExitDelay(exitDelay.getValue());
            this.exitManager.setHapiExitCode(exitCode.getValue());
        }
        return parameters;
    }
}