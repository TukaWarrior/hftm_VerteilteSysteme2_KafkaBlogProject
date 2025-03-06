package ch.hftm.blogproject.messaging;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniEmitter;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Message;


@ApplicationScoped
public class ProfanityValidationConsumer {

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<UniEmitter<? super Boolean>>> emitters = 
        new ConcurrentHashMap<>();

    @Inject
    Vertx vertx;

    @Incoming("profanity-validation-in")
    public Uni<Void> processValidation(Message<String> message) {
        return Uni.createFrom().item(() -> new JsonObject(message.getPayload()))
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
            .chain(validation -> {
                String text = validation.getString("text");
                boolean isValid = validation.getBoolean("isValid");

                return vertx.executeBlocking(() -> {
                    if (emitters.containsKey(text)) {
                        emitters.get(text).forEach(emitter -> emitter.complete(isValid));
                        emitters.remove(text);
                    }
                    return null;
                });
            })
            .replaceWithVoid()
            .onTermination().invoke(() -> message.ack());
    }

    public Uni<Boolean> waitForValidation(String text) {
        return Uni.createFrom().<Boolean>emitter(emitter -> {
                emitters.computeIfAbsent(text, k -> new CopyOnWriteArrayList<>()).add(emitter);
            })
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }
}