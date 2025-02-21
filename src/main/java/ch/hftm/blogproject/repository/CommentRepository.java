package ch.hftm.blogproject.repository;

import ch.hftm.blogproject.model.entity.Comment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

// This class serves as the interface between the CommentService class and the MySQL Database.

@ApplicationScoped
public class CommentRepository implements PanacheRepository<Comment> {
}
