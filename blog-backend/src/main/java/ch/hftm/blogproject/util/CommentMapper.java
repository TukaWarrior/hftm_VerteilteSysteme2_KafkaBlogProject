package ch.hftm.blogproject.util;

import ch.hftm.blogproject.model.dto.CommentDTO;
import ch.hftm.blogproject.model.entity.Comment;

public class CommentMapper {
    
    public static Comment toCommentEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setCommentID(commentDTO.getCommentID());
        comment.setBlogID(commentDTO.getBlogID());
        comment.setContent(commentDTO.getContent());
        comment.setCreator(commentDTO.getCreator());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        comment.setLastChangedAt(commentDTO.getLastChangedAt());
        return comment;
    }
    
    public static CommentDTO toCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentID(commentDTO.getCommentID());
        commentDTO.setBlogID(commentDTO.getBlogID());
        commentDTO.setContent(commentDTO.getContent());
        commentDTO.setCreator(commentDTO.getCreator());
        commentDTO.setCreatedAt(commentDTO.getCreatedAt());
        commentDTO.setLastChangedAt(commentDTO.getLastChangedAt());
        return commentDTO;
    }
}
