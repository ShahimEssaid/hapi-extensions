package com.essaid.fhir.hapiext.server.provider;

import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import com.essaid.fhir.hapiext.Properties;
import com.essaid.fhir.hapiext.server.ExitManager;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.stereotype.Component;

@Component
public class ShutdownOperation {
    public static final String SHUTDOWN_PASSWORD_KEY = "password";

    private final ExitManager exitManager;
    private final Properties extensionsProperties;

    public ShutdownOperation(Properties extensionsProperties, ExitManager exitManager) {
        this.exitManager = exitManager;
        this.extensionsProperties = extensionsProperties;
    }

    @Operation(name = "shutdown", idempotent = true)
    public Parameters shutdown(@OperationParam(name = "exit-code", typeName = "integer", min = 1, max = 1) IPrimitiveType<Integer> exitCode,
                               @OperationParam(name = "password", typeName = "string", min = 1, max = 1) IPrimitiveType<String> password,
                               @OperationParam(name = "exit-delay", typeName = "integer", min = 1, max = 1) IPrimitiveType<Integer> exitDelay) {
        Parameters parameters = new Parameters();
        String configuredPassword = extensionsProperties.getProvidersConfiguration().get(ShutdownOperation.class.getName()).get(SHUTDOWN_PASSWORD_KEY).toString();
        if (password == null || !password.getValue().equals(configuredPassword)) {
            parameters.addParameter().setName("Password").setValue(new StringType("Not set or not matching: " + (password != null ? password.getValue() : "Null")));
        } else {
            parameters.addParameter().setName("Shutting down").setValue(new StringType("True"));
            this.exitManager.setHapiExitDelay(exitDelay.getValue());
            this.exitManager.setHapiExitCode(exitCode.getValue());
        }
        return parameters;
    }
}