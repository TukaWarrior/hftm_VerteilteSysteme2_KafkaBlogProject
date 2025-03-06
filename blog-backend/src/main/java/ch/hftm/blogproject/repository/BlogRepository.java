package ch.hftm.blogproject.repository;

import java.util.List;
import ch.hftm.blogproject.model.entity.Blog;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogRepository implements PanacheRepository<Blog> {

    // Get all blogs
    public List<Blog> findAllBlogs() {
        return this.listAll();
    }

    // Get a blog by ID
    public Blog findBlogsById(Long id) {
        return this.findById(id);
    }

    // Add a new blog
    public void persistBlog(Blog blog) {
        this.persist(blog);
    }

    // Update an existing blog
    public void updateBlog(Blog blog) {
        this.persist(blog);
    }

    // Delete a blog by ID
    public void deleteBlogById(Long id) {
        this.deleteById(id);
    }

    // Delete all blogs
    public void deleteAllBlogs() {
        this.deleteAll();
    }

    // Count all blogs
    public Long countBlogs() {
        return this.count();
    }
}