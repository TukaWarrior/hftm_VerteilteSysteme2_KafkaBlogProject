package ch.hftm.blogproject.util;

import ch.hftm.blogproject.model.dto.CommentDTO;
import ch.hftm.blogproject.model.entity.Comment;

public class CommentMapper {
       public static CommentDTO toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return new CommentDTO() {
            {
                setCommentID(comment.getCommentID());
                setBlogPostID(comment.getBlogPostID());
                setContent(comment.getContent());
                setCreator(comment.getCreator());
                setCreatedAt(comment.getCreatedAt());
                setLastChangedAt(comment.getLastChangedAt());
            }
        };
    }

    public static Comment toComment(CommentDTO commentDTO) {
        if (commentDTO == null) {
            return null;
        }
        return new Comment() {
            {
                setCommentID(commentDTO.getCommentID());
                setBlogPostID(commentDTO.getBlogPostID());
                setContent(commentDTO.getContent());
                setCreator(commentDTO.getCreator());
                setCreatedAt(commentDTO.getCreatedAt());
                setLastChangedAt(commentDTO.getLastChangedAt());
            }
        };
    }
}
