package ch.hftm.blogproject.control;

import java.time.ZonedDateTime;
import java.util.List;

import ch.hftm.blogproject.messaging.ProfanityValidationConsumer;
import ch.hftm.blogproject.messaging.ProfanityValidationProducer;
import ch.hftm.blogproject.model.dto.BlogDTO;
import ch.hftm.blogproject.model.entity.Blog;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.repository.BlogRepository;
import ch.hftm.blogproject.repository.CommentRepository;
import ch.hftm.blogproject.util.BlogMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BlogService {

    @Inject
    BlogRepository blogRepository;
    @Inject
    CommentRepository commentRepository;
    @Inject
    ProfanityValidationProducer blogValidationProducer;
    @Inject
    ProfanityValidationConsumer blogValidationConsumer;
    @Inject
    io.vertx.mutiny.core.Vertx vertx;

    // Get all blogs
    // @WithSession
    public Uni<List<BlogDTO>> getAllBlogs() {
        return blogRepository.findAllBlogs()
            .onItem().transform(blogs -> blogs.stream()
                .map(BlogMapper::toBlogDTO)
                .toList());
    }

    // Get a blog by ID
    // @WithSession
    public Uni<BlogDTO> getBlogById(Long blogID) {
        return blogRepository.findBlogsById(blogID)
            .onItem().ifNull().failWith(() -> new NotFoundException("Blog with ID " + blogID + " not found."))
            .onItem().transform(BlogMapper::toBlogDTO);
    }

    // Add a new blog
    // @WithTransaction
    public Uni<BlogDTO> addBlog(BlogDTO blogDTO) {
        // Combine title and content into a single string for validation
        String textToValidate = blogDTO.getTitle() + " " + blogDTO.getContent();

        // Send the text to Kafka for validation
        System.out.println("Sending text to BlogValidationProducer");
        blogValidationProducer.sendTextForValidation(textToValidate);

        // Wait for validation response
        return blogValidationConsumer.waitForValidation(textToValidate)
            .emitOn(vertx.getOrCreateContext().runOnContext(v -> {})) // Switch to the Vert.x event loop thread
            .onItem().transformToUni(isValid -> {
                if (isValid) {
                    // If valid, persist the blog in the database
                    Blog blog = BlogMapper.toBlogEntity(blogDTO);
                    blog.setCreatedAt(ZonedDateTime.now());
                    blog.setValidated(true); // Mark as validated
                    return blogRepository.persistBlog(blog)
                        .onItem().transform(BlogMapper::toBlogDTO);
                } else {
                    // If invalid, throw an error
                    return Uni.createFrom().failure(new IllegalArgumentException("Blog content is invalid."));
                }
            });
    }

    // Update an existing blog
    // @WithTransaction
    public Uni<BlogDTO> updateBlog(BlogDTO blogDTO) {
        Blog blog = BlogMapper.toBlogEntity(blogDTO);
        blog.setLastChangedAt(ZonedDateTime.now());
        return blogRepository.updateBlog(blog)
            .onItem().ifNull().failWith(() -> new NotFoundException("Blog with ID " + blogDTO.getBlogID() + " not found."))
            .onItem().transform(BlogMapper::toBlogDTO);
    }

    // Delete a blog by ID (and its associated comments)
    // @WithTransaction
    public Uni<Void> deleteBlog(Long blogID) {
        return blogRepository.findBlogsById(blogID)
            .onItem().ifNull().failWith(() -> new NotFoundException("Blog with ID " + blogID + " not found."))
            .onItem().transformToUni(blog -> 
                commentRepository.deleteCommentsByBlogID(blogID) // Delete associated comments
                    .onItem().transformToUni(ignored -> blogRepository.deleteBlogById(blogID))
            )
            .replaceWithVoid();
    }

    // Delete all blogs (and their associated comments)
    // @WithTransaction
    public Uni<Void> deleteAllBlogs() {
        return commentRepository.deleteAllComments() // Delete all comments first
            .onItem().transformToUni(ignored -> blogRepository.deleteAllBlogs());
    }

    // Count all blogs
    // @WithSession
    public Uni<Long> countBlogs() {
        return blogRepository.countBlogs();
    }
}
