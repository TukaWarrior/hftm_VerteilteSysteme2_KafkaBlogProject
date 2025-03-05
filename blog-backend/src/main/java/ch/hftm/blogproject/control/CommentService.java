package ch.hftm.blogproject.control;

import java.time.ZonedDateTime;
import java.util.List;

import ch.hftm.blogproject.model.entity.Comment;
import ch.hftm.blogproject.model.dto.CommentDTO;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.repository.BlogRepository;
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
    @Inject
    BlogRepository blogRepository;

    // Get all comments
    @WithSession
    public Uni<List<CommentDTO>> getAllComments() {
        return commentRepository.findAllComments()
            .onItem().transform(comments -> comments.stream()
                .map(CommentMapper::toCommentDTO)
                .toList());
    }

    // Get all comments by blog ID
    @WithSession
    public Uni<List<CommentDTO>> getCommentsByBlogId(Long blogID) {
        return commentRepository.findCommentsByBlogId(blogID)
            .onItem().transform(comments -> comments.stream()
            .map(CommentMapper::toCommentDTO)
            .toList());
    }

    // Get a comment by ID
    @WithSession
    public Uni<CommentDTO> getCommentById(Long commentID) {
        return commentRepository.findCommentById(commentID)
            .onItem().ifNull().failWith(() -> new NotFoundException("Comment with ID " + commentID + " not found."))
            .onItem().transform(CommentMapper::toCommentDTO);
    }


    // Add a new comment
    @WithTransaction
    public Uni<CommentDTO> addComment(CommentDTO commentDTO) {
        if (commentDTO.getBlogID() == null) {
         throw new IllegalArgumentException("Blog ID is required for a comment.");
        }
        return blogRepository.findById(commentDTO.getBlogID())
            .onItem().ifNull().failWith(() -> new NotFoundException("Blog with ID " + commentDTO.getBlogID() + " does not exist."))
            .chain(blog -> {
                Comment comment = CommentMapper.toCommentEntity(commentDTO);
                comment.setCreatedAt(ZonedDateTime.now());
                return commentRepository.persistComment(comment)
                    .onItem().transform(CommentMapper::toCommentDTO);
            });
    }

    // Update an existing comment
    @WithTransaction
    public Uni<CommentDTO> updateComment(CommentDTO commentDTO) {
        Comment comment = CommentMapper.toCommentEntity(commentDTO);
        comment.setLastChangedAt(ZonedDateTime.now());
        return commentRepository.updateComment(comment)
            .onItem().ifNull().failWith(() -> new NotFoundException("Comment with ID " + commentDTO.getCommentID() + " not found."))
            .onItem().transform(CommentMapper::toCommentDTO);
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
