package ch.hftm.blogproject.messaging;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import ch.hftm.blogproject.model.entity.Blog;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogValidationProducer {

    @Channel("blog-validation-request")
    Emitter<String> emitter;

    public void sendBlogForValidation(Blog blog) {
        // Create a JSON representation of the blog data
        String message = String.format(
            "{\"blogID\":%d,\"title\":\"%s\",\"content\":\"%s\"}",
            blog.getBlogID(), blog.getTitle(), blog.getContent()
        );

        // Send the message to the Kafka topic
        emitter.send(message);
    }
}