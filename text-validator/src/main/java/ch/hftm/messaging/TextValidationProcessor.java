package ch.hftm.messaging;

import java.util.Set;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TextValidationProcessor {

    // Define a set of banned words
    private static final Set<String> BANNED_WORDS = Set.of("banned", "prohibited", "restricted");

    @Incoming("profanity-validation-out")
    @Outgoing("profanity-validation-in")
    public String validateText(String message) {
        System.out.println("Received message: " + message);
        JsonObject json = new JsonObject(message);
        String text = json.getString("text");

        boolean isValid = isTextValid(text);

        System.out.println("Text is " + (isValid ? "valid" : "invalid"));
        return new JsonObject()
            .put("text", text)
            .put("isValid", isValid)
            .toString();
    }

    private boolean isTextValid(String text) {
        for (String bannedWord : BANNED_WORDS) {
            if (text.toLowerCase().contains(bannedWord.toLowerCase())) {
                return false; // Invalid if any banned word is found
            }
        }
        return true; // Valid if no banned words are found
    }
}
