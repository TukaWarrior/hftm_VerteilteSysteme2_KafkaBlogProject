package ch.hftm.blogproject.control;

import java.time.ZonedDateTime;
import java.util.List;

import ch.hftm.blogproject.model.entity.Comment;
import ch.hftm.blogproject.model.dto.CommentDTO;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.repository.BlogRepository;
import ch.hftm.blogproject.repository.CommentRepository;
import ch.hftm.blogproject.util.CommentMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CommentService {

    @Inject
    CommentRepository commentRepository;
    @Inject
    BlogRepository blogRepository;

    // Get all comments
    public List<CommentDTO> getAllComments() {
        return commentRepository.findAllComments().stream()
            .map(CommentMapper::toCommentDTO)
            .toList();
    }

    // Get all comments by blog ID
    public List<CommentDTO> getCommentsByBlogId(Long blogID) {
        return commentRepository.findCommentsByBlogId(blogID).stream()
            .map(CommentMapper::toCommentDTO)
            .toList();
    }

    // Get a comment by ID
    public CommentDTO getCommentById(Long commentID) {
        Comment comment = commentRepository.findCommentById(commentID);
        if (comment == null) {
            throw new NotFoundException("Comment with ID " + commentID + " not found.");
        }
        return CommentMapper.toCommentDTO(comment);
    }

    // Add a new comment
    public CommentDTO addComment(CommentDTO commentDTO) {
        if (commentDTO.getBlogID() == null) {
            throw new IllegalArgumentException("Blog ID is required for a comment.");
        }
        if (blogRepository.findBlogsById(commentDTO.getBlogID()) == null) {
            throw new NotFoundException("Blog with ID " + commentDTO.getBlogID() + " does not exist.");
        }
        Comment comment = CommentMapper.toCommentEntity(commentDTO);
        comment.setCreatedAt(ZonedDateTime.now());
        commentRepository.persistComment(comment);
        return CommentMapper.toCommentDTO(comment);
    }

    // Update an existing comment
    public CommentDTO updateComment(CommentDTO commentDTO) {
        Comment existingComment = commentRepository.findCommentById(commentDTO.getCommentID());
        if (existingComment == null) {
            throw new NotFoundException("Comment with ID " + commentDTO.getCommentID() + " not found.");
        }
        existingComment.setContent(commentDTO.getContent());
        existingComment.setCreator(commentDTO.getCreator());
        existingComment.setLastChangedAt(ZonedDateTime.now());
        commentRepository.updateComment(existingComment);
        return CommentMapper.toCommentDTO(existingComment);
    }

    // Delete a comment by ID
    public void deleteComment(Long commentID) {
        Comment comment = commentRepository.findCommentById(commentID);
        if (comment == null) {
            throw new NotFoundException("Comment with ID " + commentID + " not found.");
        }
        commentRepository.deleteCommentById(commentID);
    }

    // Delete all comments
    public void deleteAllComments() {
        commentRepository.deleteAllComments();
    }

    // Count all comments
    public Long countComments() {
        return commentRepository.countComments();
    }
}
