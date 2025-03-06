package ch.hftm.blogproject.repository;

import java.util.List;

import ch.hftm.blogproject.model.entity.Comment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

// This class serves as the interface between the CommentService class and the MySQL Database.

@ApplicationScoped
public class CommentRepository implements PanacheRepository<Comment> {

    // Get all comments
    public List<Comment> findAllComments() {
        return this.listAll();
    }

    // Get all comments by blog ID
    public List<Comment> findCommentsByBlogId(Long blogID) {
        return this.list("blogID", blogID);
    }

    // Get a comment by ID
    public Comment findCommentById(Long id) {
        return this.findById(id);
    }

    // Add a new comment
    public void persistComment(Comment comment) {
        this.persist(comment);
    }

    // Update an existing comment
    public void updateComment(Comment comment) {
        this.persist(comment);
    }

    // Delete a comment by ID
    public void deleteCommentById(Long id) {
        this.deleteById(id);
    }

    // Delete all comments
    public void deleteAllComments() {
        this.deleteAll();
    }

    // Count all comments
    public Long countComments() {
        return this.count();
    }

    // Delete comments by blog ID
    public void deleteCommentsByBlogID(Long blogID) {
        this.delete("blogID", blogID);
    }
}