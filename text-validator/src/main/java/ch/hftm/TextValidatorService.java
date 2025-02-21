package ch.hftm;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TextValidatorService {

    @Incoming("validation-request")
    @Outgoing("validation-response")
    public Multi<ValidationResponse> validateTextMessages(Multi<ValidationRequest> requests) {
        return requests
                .onItem().transform(request -> {
                    boolean valid = !request.text().contains("hftm sucks"); // Example validation logic
                    Log.debug("Text-Validation: " + request.text() + " -> " + valid);
                    return new ValidationResponse(request.id(), valid);
                });
    }
}
