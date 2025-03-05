package ch.hftm.blogproject.messaging;

import ch.hftm.blogproject.model.dto.BlogDTO;
import  org.eclipse.microprofile.reactive.messaging.Channel ;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogProducer {

    @Channel("blogs")
    Emitter<BlogDTO> emitter;

    public void sendBlog(BlogDTO blogDTO) {
        emitter.send(blogDTO);
    }
}