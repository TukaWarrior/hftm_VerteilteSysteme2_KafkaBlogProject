package ch.hftm.messaging;

import java.util.Arrays;
import java.util.List;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogValidationService {

    // List of profane words
    private static final List<String> PROFANE_WORDS = Arrays.asList(
        "bad",
        "worse",
        "angular"
    );

    @Channel("blog-validation-response")
    Emitter<String> responseEmitter;

    @Incoming("blog-validation-request")
    public void validateBlog(String message) {
        System.out.println("Received blog for validation: " + message);

        // Parse the incoming JSON message
        JsonObject json = new JsonObject(message);
        Long blogID = json.getLong("blogID");
        String title = json.getString("title");
        String content = json.getString("content");

        // Check for profanity
        boolean isValid = !containsProfanity(title) && !containsProfanity(content);

        // Create the response JSON
        JsonObject response = new JsonObject()
            .put("blogID", blogID)
            .put("isValidated", isValid);


// ------------------------------| DELAY FOR TESTING PURPOSES |------------------------------
// ------------------------------------------------------------------------------------------
// This delay is added to visually show, that blogs in the blog-backend are initially not validated.
// Because the entire validation and kafka messaging process is so fast, it is hard to notice without a delay.
// Comment this delay out to run the validator in real time.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
// ------------------------------------------------------------------------------------------


        // Send the response to the Kafka topic
        responseEmitter.send(response.encode());
        System.out.println("Sent validation response for blogID: " + blogID + ", isValidated: " + isValid);
    }

    // Method to check for profanity in a string
    private boolean containsProfanity(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        for (String profaneWord : PROFANE_WORDS) {
            if (text.toLowerCase().contains(profaneWord.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}