package ch.hftm.blogproject.control;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import ch.hftm.blogproject.model.dto.CommentDTO;
import ch.hftm.blogproject.model.entity.BlogPost;
import ch.hftm.blogproject.model.entity.Comment;
import ch.hftm.blogproject.model.exception.DatabaseException;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.repository.BlogPostRepository;
import ch.hftm.blogproject.repository.CommentRepository;
import ch.hftm.blogproject.util.DTOConverter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CommentService {

    @Inject
    CommentRepository commentRepository;
    @Inject
    BlogPostRepository blogPostRepository;

    // Get all comments with optional filtering
    public List<CommentDTO> getComments(Optional<String> searchString, Optional<Integer> page) {
        PanacheQuery<Comment> commentQuery;
        try {
            if (searchString.isEmpty()) {
                commentQuery = commentRepository.findAll();
            } else {
                commentQuery = commentRepository.find("content like ?1", "%" + searchString.get() + "%");
            }
            int pageNumber = page.orElse(0);
            List<Comment> comments = commentQuery.page(Page.of(pageNumber, 10)).list();
            return DTOConverter.toCommentDtoList(comments);
        } catch (Exception e) {
            throw new DatabaseException("Error while getting comments.", e);
        }
    }

    // Get all comments for a specific blog post
    public List<CommentDTO> getAllCommentsOfBlogPost(Long blogPostID) {
        try {
            BlogPost blogPost = blogPostRepository.findById(blogPostID);
            if (blogPost == null) {
                throw new NotFoundException("Blog post with ID " + blogPostID + " not found.");
            }
            return DTOConverter.toCommentDtoList(blogPost.getComments());
        } catch (Exception e) {
            throw new DatabaseException("Error while getting comments from blog with ID " + blogPostID, e);
        }
    }

    // Get a single comment by ID
    public CommentDTO getCommentById(Long commentID) {
        try {
            Comment comment = commentRepository.findById(commentID);
            if (comment == null) {
                throw new NotFoundException("Comment with ID " + commentID + " not found.");
            }
            return DTOConverter.toCommentDto(comment);
        } catch (Exception e) {
            throw new DatabaseException("Error while getting comment with ID " + commentID, e);
        }
    }

    // Get a single comment from a specific blog post
    public CommentDTO getCommentFromBlogPost(Long blogPostID, Long commentID) {
        try {
            BlogPost blogPost = blogPostRepository.findById(blogPostID);
            if (blogPost == null) {
                throw new NotFoundException("Blog post with ID " + blogPostID + " not found.");
            }
            Comment comment = commentRepository.findById(commentID);
            if (comment == null || !comment.getBlogPost().getBlogPostID().equals(blogPostID)) {
                throw new NotFoundException("Comment with ID " + commentID + " not found for blog post with ID " + blogPostID);
            }
            return DTOConverter.toCommentDto(comment);
        } catch (Exception e) {
            throw new DatabaseException("Error while getting comment with ID " + commentID + " from blog with ID " + blogPostID, e);
        }
    }  

    // Add a new comment to a blog post
    @Transactional
    public CommentDTO addCommentToBlog(Long blogPostID, CommentDTO commentDTO) {
        try {
            BlogPost blogPost = blogPostRepository.findById(blogPostID);
            if (blogPost == null) {
                throw new NotFoundException("Blog post with ID " + blogPostID + " not found.");
            }
            // Calculate next comment number for the blog post
            // Long nextCommentNumber = commentRepository.count("blog.id", blogPostID) + 1;
            Comment comment = new Comment();
            // comment.setCommentNumber(nextCommentNumber); // Comment number specific to this blog post
            comment.setContent(commentDTO.getContent());
            comment.setCreator(commentDTO.getCreator());
            comment.setCreatedAt(ZonedDateTime.now());
            comment.setBlogPost(blogPost);

            commentRepository.persist(comment);
            return DTOConverter.toCommentDto(comment);
        } catch (Exception e) {
            throw new DatabaseException("Error while adding comment to blog post with ID " + blogPostID, e);
        }
    }

    // Update an existing comment for a specific blog post
    @Transactional
    public CommentDTO updateCommentOfBlog(Long blogPostID, CommentDTO commentDTO) {
        Long commentID = commentDTO.getCommentID();

        try {
            BlogPost existingBlog = blogPostRepository.findById(blogPostID);
            if (existingBlog == null) {
                throw new NotFoundException("Blog post with ID " + blogPostID + " not found.");
            }
            Comment existingComment = commentRepository.findById(commentID);
            if (existingComment == null || !existingComment.getBlogPost().getBlogPostID().equals(blogPostID)) {
                throw new NotFoundException("Comment with ID " + commentID + " not found for blog post with ID " + blogPostID);
            }
            existingComment.setContent(commentDTO.getContent());
            existingComment.setLastChangedAt(ZonedDateTime.now());
            commentRepository.persist(existingComment);
            return DTOConverter.toCommentDto(existingComment);
        } catch (Exception e) {
            throw new DatabaseException("Error while updating comment with ID " + commentID + " for blog post with ID " + blogPostID, e);
        }
    }

    // Delete a comment from a specific blog post
    @Transactional
    public CommentDTO deleteCommentFromBlog(Long blogPostID, Long commentID) {
        try {
            BlogPost existingBlogPost = blogPostRepository.findById(blogPostID);
            if (existingBlogPost == null) {
                throw new NotFoundException("Blog post with ID " + blogPostID + " not found.");
            }
            Comment existingComment = commentRepository.findById(commentID);
            if (existingComment == null || !existingComment.getBlogPost().getBlogPostID().equals(blogPostID)) {
                throw new NotFoundException("Comment with ID " + commentID + " not found for blog post with ID " + blogPostID);
            }

            commentRepository.delete(existingComment);
            return DTOConverter.toCommentDto(existingComment);
        } catch (Exception e) {
            throw new DatabaseException("Error while deleting comment with ID " + commentID + " from blog post with ID " + blogPostID, e);
        }
    }

    // Count all comments
    public Long countComments() {
        try {
            return commentRepository.count();
        } catch (Exception e) {
            throw new DatabaseException("Error while counting all comments.", e);
        }
    }

    // Count all comments for a specific blog post
    public Long countCommentsFromBlogPost(Long blogPostID) {
        try {
            BlogPost blogPost = blogPostRepository.findById(blogPostID);
            if (blogPost == null) {
                throw new NotFoundException("Blog post with ID " + blogPostID + " not found.");
            }
            return commentRepository.count("blogPost.id", blogPostID);
        } catch (Exception e) {
            throw new DatabaseException("Error while counting comments for blog post with ID " + blogPostID, e);
        }
    }

    // Delete all comments (admin operation)
    @Transactional
    public void deleteAllComments() {
        try {
            commentRepository.deleteAll();
        } catch (Exception e) {
            throw new DatabaseException("Error while deleting all comments.", e);
        }
    }

    // Get comments within a specific date range
    public List<CommentDTO> getCommentsByDateRange(ZonedDateTime startDate, ZonedDateTime endDate) {
        try {
            List<Comment> comments = commentRepository.find("createdAt BETWEEN ?1 AND ?2", startDate, endDate).list();
            return DTOConverter.toCommentDtoList(comments);
        } catch (Exception e) {
            throw new DatabaseException("Error while getting comments by date range.", e);
        }
    }
}
