package ch.hftm.blogproject.repository;

import java.time.ZonedDateTime;
import java.util.List;

import ch.hftm.blogproject.model.entity.BlogPost;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogPostRepository implements PanacheRepository<BlogPost>{
    
    // Get all blog posts
    public Uni<List<BlogPost>> findAllBlogPosts() {
        return this.listAll();
    }

    // Get a blog post by ID
    public Uni<BlogPost> findBlogPostById(Long id) {
        return this.findById(id);
    }

    // Add a new blog post
    public Uni<BlogPost> persistBlogPost(BlogPost blogPost) {
        return this.persist(blogPost).replaceWith(blogPost);
    }

    // Update an existing blog post
    public Uni<BlogPost> updateBlogPost(BlogPost blogPost) {
        return this.findById(blogPost.getBlogPostID())
            .onItem().ifNotNull().invoke(existingBlogPost -> {
                existingBlogPost.setTitle(blogPost.getTitle());
                existingBlogPost.setContent(blogPost.getContent());
                existingBlogPost.setCreator(blogPost.getCreator());
                existingBlogPost.setLastChangedAt(ZonedDateTime.now());
            })
            .onItem().ifNotNull().transformToUni(existingBlogPost -> this.persist(existingBlogPost))
            .replaceWith(blogPost);
    }

    // Delete a blog post by ID
    public Uni<Boolean> deleteBlogPostById(Long id) {
        return this.deleteById(id);
    }

    // Delete all blog posts
    public Uni<Void> deleteAllBlogPosts() {
        return this.deleteAll().replaceWithVoid();
    }

    // Count all blog posts
    public Uni<Long> countBlogPosts() {
        return this.count();
    }
}
