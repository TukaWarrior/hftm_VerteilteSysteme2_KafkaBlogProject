package ch.hftm.messaging;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogValidationProcess {

    @Blocking
    @Incoming("blogs")
    @Outgoing("validations")
    public String validateBlog(String blogMessage) {
        // Parse the blog message (assume it's in JSON format)
        JsonObject blog = new JsonObject(blogMessage);
        String title = blog.getString("title");
        String content = blog.getString("content");

        // Perform validation (e.g., check for banned words)
        boolean isValid = !content.contains("banned");

        // Return validation result as a JSON string
        return new JsonObject()
            .put("title", title)
            .put("content", content)
            .put("isValid", isValid)
            .toString();
    }
}

