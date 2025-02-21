package ch.hftm.blogproject.util;

import java.util.List;
import java.util.stream.Collectors;

import ch.hftm.blogproject.model.dto.BlogPostDTO;
import ch.hftm.blogproject.model.dto.CommentDTO;
import ch.hftm.blogproject.model.entity.BlogPost;
import ch.hftm.blogproject.model.entity.Comment;

public class DTOConverter {
    
    public static List<BlogPostDTO> toBlogPostDtoList(List<BlogPost> blogPosts) {
        if (blogPosts == null || blogPosts.isEmpty()) {
            return null;
        }
        return blogPosts.stream().map(blogPost -> new BlogPostDTO() {
            {
            setBlogPostID(blogPost.getBlogPostID());
            setTitle(blogPost.getTitle());
            setContent(blogPost.getContent());
            setCreator(blogPost.getCreator());
            setCreatedAt(blogPost.getCreatedAt());
            setLastChangedAt(blogPost.getLastChangedAt());
            setComments(toCommentDtoList(blogPost.getComments()));
            }
        }).collect(Collectors.toList());
    }

    public static List<CommentDTO> toCommentDtoList(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return null;
        }
        return comments.stream().map(comment -> new CommentDTO() {
            {
                setCommentID(comment.getCommentID());
                setBlogPostID(comment.getBlogPost().getBlogPostID());
                setContent(comment.getContent());
                setCreator(comment.getCreator());
                setCreatedAt(comment.getCreatedAt());
                setLastChangedAt(comment.getLastChangedAt());
            }
        }).collect(Collectors.toList());
    }

    public static List<Comment> toCommentList(List<CommentDTO> commentDTOs) {
        if (commentDTOs == null || commentDTOs.isEmpty()) {
            return null;
        }
        return commentDTOs.stream().map(commentDTO -> new Comment() {
            {
                setCommentID(commentDTO.getCommentID());
                setContent(commentDTO.getContent());
                setCreator(commentDTO.getCreator());
                setCreatedAt(commentDTO.getCreatedAt());
                setLastChangedAt(commentDTO.getLastChangedAt());
            }
        }).collect(Collectors.toList());
    }

    public static BlogPostDTO toBlogPostDto(BlogPost blogPost) {
        if (blogPost == null) {
            return null;
        }
        return new BlogPostDTO() {
            {
                setBlogPostID(blogPost.getBlogPostID());
                setTitle(blogPost.getTitle());
                setContent(blogPost.getContent());
                setCreator(blogPost.getCreator());
                setCreatedAt(blogPost.getCreatedAt());
                setLastChangedAt(blogPost.getLastChangedAt());
                setComments(toCommentDtoList(blogPost.getComments()));
            }
        };
    }

    public static BlogPost toBlogPost(BlogPostDTO blogPostDTO) {
        if (blogPostDTO == null) {
            return null;
        }
        return new BlogPost() {
            {
                setBlogPostID(blogPostDTO.getBlogPostID());
                setTitle(blogPostDTO.getTitle());
                setContent(blogPostDTO.getContent());
                setCreator(blogPostDTO.getCreator());
                setCreatedAt(blogPostDTO.getCreatedAt());
                setLastChangedAt(blogPostDTO.getLastChangedAt());
                setComments(toCommentList(blogPostDTO.getComments()));
            }
        };
    }

    public static CommentDTO toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return new CommentDTO() {
            {
                setCommentID(comment.getCommentID());
                setBlogPostID(comment.getBlogPost().getBlogPostID());
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
                setContent(commentDTO.getContent());
                setCreator(commentDTO.getCreator());
                setCreatedAt(commentDTO.getCreatedAt());
                setLastChangedAt(commentDTO.getLastChangedAt());
            }
        };
    }

    public static List<Comment> setBlogPostReferenceOnComment(BlogPost blogPost, List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return null;
        }
        for (Comment comment : comments) {
            comment.setBlogPost(blogPost);
        }
        return comments;
    }
}
