package ch.hftm.blogproject.repository;

import java.time.ZonedDateTime;
import java.util.List;

import ch.hftm.blogproject.model.entity.Blog;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogRepository implements PanacheRepository<Blog>{
    
    // Get all blogs
    @WithSession
    public Uni<List<Blog>> findAllBlogs() {
        return this.listAll();
    }

    // Get a blog by ID
    @WithSession
    public Uni<Blog> findBlogsById(Long id) {
        return this.findById(id);
    }

    // Add a new blog
    @WithTransaction
    public Uni<Blog> persistBlog(Blog blog) {
        return this.persist(blog).replaceWith(blog);
    }

    // Update an existing blogs
    @WithTransaction
    public Uni<Blog> updateBlog(Blog blog) {
        return this.findById(blog.getBlogID())
            .onItem().ifNotNull().invoke(existingBlog -> {
                existingBlog.setTitle(blog.getTitle());
                existingBlog.setContent(blog.getContent());
                existingBlog.setCreator(blog.getCreator());
                existingBlog.setLastChangedAt(ZonedDateTime.now());
            })
            .onItem().ifNotNull().transformToUni(existingBlog -> this.persist(existingBlog))
            .replaceWith(blog);
    }

    // Delete a blog by ID
    @WithTransaction
    public Uni<Boolean> deleteBlogById(Long id) {
        return this.deleteById(id);
    }

    // Delete all blogs
    @WithTransaction
    public Uni<Void> deleteAllBlogs() {
        return this.deleteAll().replaceWithVoid();
    }

    // Count all blogs
    @WithSession
    public Uni<Long> countBlogs() {
        return this.count();
    }
}
