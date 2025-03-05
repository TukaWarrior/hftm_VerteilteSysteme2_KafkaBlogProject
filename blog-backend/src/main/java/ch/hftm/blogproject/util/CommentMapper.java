package ch.hftm.blogproject.util;

import java.time.ZonedDateTime;

import ch.hftm.blogproject.model.dto.CommentDTO;
import ch.hftm.blogproject.model.entity.Comment;

public class CommentMapper {
    
    public static Comment toCommentEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setBlogID(commentDTO.getBlogID());
        comment.setContent(commentDTO.getContent());
        comment.setCreator(commentDTO.getCreator());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        comment.setLastChangedAt(ZonedDateTime.now());
        return comment;
    }
    
    public static CommentDTO toCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentID(comment.getCommentID());
        commentDTO.setBlogID(comment.getBlogID());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreator(comment.getCreator());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setLastChangedAt(comment.getLastChangedAt());
        return commentDTO;
    }
}
