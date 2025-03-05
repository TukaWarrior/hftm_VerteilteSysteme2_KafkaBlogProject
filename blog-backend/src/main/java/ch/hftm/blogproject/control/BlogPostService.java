package ch.hftm.blogproject.control;

import java.time.ZonedDateTime;
import java.util.List;

import ch.hftm.blogproject.model.dto.BlogPostDTO;
import ch.hftm.blogproject.model.entity.BlogPost;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.repository.BlogPostRepository;
import ch.hftm.blogproject.repository.CommentRepository;
import ch.hftm.blogproject.util.BlogPostMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BlogPostService {

    @Inject
    BlogPostRepository blogPostRepository;

    @Inject
    CommentRepository commentRepository;

    // Get all blog posts
    @WithSession
    public Uni<List<BlogPostDTO>> getAllBlogPosts() {
        return blogPostRepository.findAllBlogPosts()
            .onItem().transform(blogPosts -> blogPosts.stream()
                .map(BlogPostMapper::toBlogPostDto)
                .toList());
    }

    // Get a blog post by ID
    @WithSession
    public Uni<BlogPostDTO> getBlogPostById(Long blogPostID) {
        return blogPostRepository.findBlogPostById(blogPostID)
            .onItem().ifNull().failWith(() -> new NotFoundException("Blog post with ID " + blogPostID + " not found."))
            .onItem().transform(BlogPostMapper::toBlogPostDto);
    }

    // Add a new blog post
    @WithTransaction
    public Uni<BlogPostDTO> addBlogPost(BlogPostDTO blogPostDTO) {
        BlogPost blogPost = BlogPostMapper.toBlogPost(blogPostDTO);
        blogPost.setCreatedAt(ZonedDateTime.now());
        return blogPostRepository.persistBlogPost(blogPost)
            .onItem().transform(BlogPostMapper::toBlogPostDto);
    }

    // Update an existing blog post
    @WithTransaction
    public Uni<BlogPostDTO> updateBlogPost(BlogPostDTO blogPostDTO) {
        BlogPost blogPost = BlogPostMapper.toBlogPost(blogPostDTO);
        blogPost.setLastChangedAt(ZonedDateTime.now());
        return blogPostRepository.updateBlogPost(blogPost)
            .onItem().ifNull().failWith(() -> new NotFoundException("Blog post with ID " + blogPostDTO.getBlogPostID() + " not found."))
            .onItem().transform(BlogPostMapper::toBlogPostDto);
    }

    // Delete a blog post by ID (and its associated comments)
    @WithTransaction
    public Uni<Void> deleteBlogPost(Long blogPostID) {
        return blogPostRepository.findBlogPostById(blogPostID)
            .onItem().ifNull().failWith(() -> new NotFoundException("Blog post with ID " + blogPostID + " not found."))
            .onItem().transformToUni(blogPost -> 
                commentRepository.deleteCommentsByBlogPostId(blogPostID) // Delete associated comments
                    .onItem().transformToUni(ignored -> blogPostRepository.deleteBlogPostById(blogPostID))
            )
            .replaceWithVoid();
    }

    // Delete all blog posts (and their associated comments)
    @WithTransaction
    public Uni<Void> deleteAllBlogPosts() {
        return commentRepository.deleteAllComments() // Delete all comments first
            .onItem().transformToUni(ignored -> blogPostRepository.deleteAllBlogPosts());
    }

    // Count all blog posts
    @WithSession
    public Uni<Long> countBlogPosts() {
        return blogPostRepository.countBlogPosts();
    }
}
