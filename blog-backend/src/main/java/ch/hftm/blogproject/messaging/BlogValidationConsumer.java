package ch.hftm.blogproject.messaging;

import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniEmitter;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogValidationConsumer {

    private final ConcurrentHashMap<String, UniEmitter<? super Boolean>> validationEmitters = new ConcurrentHashMap<>();

    // Wait for a validation response
    public Uni<Boolean> waitForValidation(String title, String content) {
        String key = generateKey(title, content);
        return Uni.createFrom().emitter(emitter -> validationEmitters.put(key, emitter));
    }

    // Process validation responses from Kafka
    @Incoming("validations")
    public void processValidation(String validationMessage) {
        // Parse the validation message (assume it's in JSON format)
        JsonObject validation = new JsonObject(validationMessage);
        String title = validation.getString("title");
        String content = validation.getString("content");
        boolean isValid = validation.getBoolean("isValid");

        String key = generateKey(title, content);
        UniEmitter<? super Boolean> emitter = validationEmitters.remove(key);
        if (emitter != null) {
            emitter.complete(isValid);
        }
    }

    private String generateKey(String title, String content) {
        return title + ":" + content; // Simple key generation
    }
}
