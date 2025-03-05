package ch.hftm.blogproject.control;

import java.time.ZonedDateTime;
import java.util.List;

import ch.hftm.blogproject.model.entity.Comment;
import ch.hftm.blogproject.model.dto.CommentDTO;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.repository.CommentRepository;
import ch.hftm.blogproject.util.CommentMapper;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CommentService {

    @Inject
    CommentRepository commentRepository;

    // Get all comments
    @WithSession
    public Uni<List<CommentDTO>> getAllComments() {
        return commentRepository.findAllComments()
            .onItem().transform(comments -> comments.stream()
                .map(CommentMapper::toCommentDto)
                .toList());
    }

    // Get a comment by ID
    @WithSession
    public Uni<CommentDTO> getCommentById(Long commentID) {
        return commentRepository.findCommentById(commentID)
            .onItem().ifNull().failWith(() -> new NotFoundException("Comment with ID " + commentID + " not found."))
            .onItem().transform(CommentMapper::toCommentDto);
    }

    // Add a new comment
    @WithTransaction
    public Uni<CommentDTO> addComment(CommentDTO commentDTO) {
        Comment comment = CommentMapper.toComment(commentDTO);
        comment.setCreatedAt(ZonedDateTime.now());
        return commentRepository.persistComment(comment)
            .onItem().transform(CommentMapper::toCommentDto);
    }

    // Update an existing comment
    @WithTransaction
    public Uni<CommentDTO> updateComment(CommentDTO commentDTO) {
        Comment comment = CommentMapper.toComment(commentDTO);
        comment.setLastChangedAt(ZonedDateTime.now());
        return commentRepository.updateComment(comment)
            .onItem().ifNull().failWith(() -> new NotFoundException("Comment with ID " + commentDTO.getCommentID() + " not found."))
            .onItem().transform(CommentMapper::toCommentDto);
    }

    // Delete a comment by ID
    @WithTransaction
    public Uni<Void> deleteComment(Long commentID) {
        return commentRepository.findCommentById(commentID)
            .onItem().ifNull().failWith(() -> new NotFoundException("Comment with ID " + commentID + " not found."))
            .onItem().transformToUni(comment -> commentRepository.deleteCommentById(commentID))
            .replaceWithVoid();
    }

    // Delete all comments
    @WithTransaction
    public Uni<Void> deleteAllComments() {
        return commentRepository.deleteAllComments();
    }

    // Count all comments
    @WithSession
    public Uni<Long> countComments() {
        return commentRepository.countComments();
    }
}
