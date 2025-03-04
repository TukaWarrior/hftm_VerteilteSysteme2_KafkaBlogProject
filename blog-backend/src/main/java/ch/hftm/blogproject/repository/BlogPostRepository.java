package ch.hftm.blogproject.repository;

import java.time.ZonedDateTime;

import ch.hftm.blogproject.model.entity.BlogPost;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

// This class serves as the interface between the BlogService class and the MySQL Database.

@ApplicationScoped
public class BlogPostRepository implements PanacheRepository<BlogPost>{
    
    // Find all BlogPosts
    public Multi<BlogPost> findAllBlogPosts() {
        return this.listAll()
            .onItem().transformToMulti(blogPosts -> Multi.createFrom().iterable(blogPosts));
    }

    // Find BlogPost by ID
    public Uni<BlogPost> findBlogPostById(Long id) {
        return this.findById(id);
    }

    // Persist a new BlogPost
    public Uni<BlogPost> persistBlogPost(BlogPost blogPost) {
        return this.persist(blogPost).replaceWith(blogPost);
    }

    // Update an existing BlogPost
    public Uni<BlogPost> updateBlogPost(BlogPost blogPost) {
        return this.findById(blogPost.getBlogPostID())
            .onItem().ifNotNull().invoke(existingBlogPost -> {
                existingBlogPost.setTitle(blogPost.getTitle());
                existingBlogPost.setContent(blogPost.getContent());
                existingBlogPost.setLastChangedAt(ZonedDateTime.now());
            })
            .onItem().ifNotNull().transformToUni(existingBlogPost -> this.persist(existingBlogPost))
            .replaceWith(blogPost);
    }

    // Delete a BlogPost by ID
    public Uni<Boolean> deleteBlogPostById(Long id) {
        return this.deleteById(id);
    }

    // Delete all BlogPosts
    public Uni<Void> deleteAllBlogPosts() {
        return this.deleteAll().replaceWithVoid();
    }

    // Count all BlogPosts
    public Uni<Long> countBlogPosts() {
        return this.count();
    }

    // Find BlogPosts by Creator
    public Multi<BlogPost> findBlogPostsByCreator(String creator) {
        return this.find("creator", creator).list() // Fetch as List
            .onItem().transformToMulti(blogPosts -> Multi.createFrom().iterable(blogPosts)); // Convert List to Multi
    }

    // Find BlogPosts by Date Range
    public Multi<BlogPost> findBlogPostsByDateRange(ZonedDateTime startDate, ZonedDateTime endDate) {
        return this.find("createdAt BETWEEN ?1 AND ?2", startDate, endDate).list() // Fetch as List
            .onItem().transformToMulti(blogPosts -> Multi.createFrom().iterable(blogPosts)); // Convert List to Multi
    }
}
