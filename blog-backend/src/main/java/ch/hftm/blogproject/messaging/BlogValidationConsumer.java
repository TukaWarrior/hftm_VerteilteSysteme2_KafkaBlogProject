package ch.hftm.blogproject.messaging;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import ch.hftm.blogproject.control.BlogService;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BlogValidationConsumer {

    @Inject
    BlogService blogService;


    @Incoming("blog-validation-response")
    public void processValidationResponse(String message) {
        System.out.println("Received blog validation response: " + message);

        // Parse the JSON response
        JsonObject json = new JsonObject(message);
        Long blogID = json.getLong("blogID");
        boolean isValidated = json.getBoolean("isValidated");
        System.out.println("Parsed blogID: " + blogID + ", isValidated: " + isValidated);

        // Update the blog validation status in the database
        try {
            blogService.updateBlogValidationStatus(blogID, isValidated);
            System.out.println("Updated blog validation status for blogID: " + blogID + ", isValidated: " + isValidated);
        } catch (Exception e) {
            System.out.println("Error updating blog validation status for blogID: " + blogID + " " + e.getMessage());
        }
    }
}