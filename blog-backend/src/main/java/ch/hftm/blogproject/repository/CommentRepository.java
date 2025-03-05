package ch.hftm.blogproject.repository;

import java.util.List;

import ch.hftm.blogproject.model.entity.Comment;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

// This class serves as the interface between the CommentService class and the MySQL Database.

@ApplicationScoped
public class CommentRepository implements PanacheRepository<Comment> {

    // Get all comments
    public Uni<List<Comment>> findAllComments() {
        return this.listAll(); // Returns a Uni<List<Comment>>
    }

    // Get a comment by ID
    public Uni<Comment> findCommentById(Long id) {
        return this.findById(id);
    }

    // Add a new comment
    public Uni<Comment> persistComment(Comment comment) {
        return this.persist(comment).replaceWith(comment);
    }

    // Update an existing comment
    public Uni<Comment> updateComment(Comment comment) {
        return this.findById(comment.getCommentID())
            .onItem().ifNotNull().invoke(existingComment -> {
                existingComment.setContent(comment.getContent());
                existingComment.setCreator(comment.getCreator());
                existingComment.setLastChangedAt(comment.getLastChangedAt());
            })
            .onItem().ifNotNull().transformToUni(existingComment -> this.persist(existingComment))
            .replaceWith(comment);
    }

    // Delete a comment by ID
    public Uni<Boolean> deleteCommentById(Long id) {
        return this.deleteById(id);
    }

    // Delete all comments
    public Uni<Void> deleteAllComments() {
        return this.deleteAll().replaceWithVoid();
    }

    // Count all comments
    public Uni<Long> countComments() {
        return this.count();
    }

    public Uni<Void> deleteCommentsByBlogID(Long blogID) {
        return this.delete("blogID", blogID).replaceWithVoid();
    }
}
