package ch.hftm.blogproject.control;

import java.time.ZonedDateTime;
import java.util.List;

import ch.hftm.blogproject.messaging.BlogValidationProducer;
import ch.hftm.blogproject.model.dto.BlogDTO;
import ch.hftm.blogproject.model.entity.BlogEntity;
import ch.hftm.blogproject.model.exception.DatabaseException;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.repository.BlogRepository;
import ch.hftm.blogproject.util.BlogMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class BlogService {

    @Inject
    BlogRepository blogRepository;
    @Inject
    BlogValidationProducer blogValidationProducer;
    
    // ========================================| Retrieval Methods |========================================
    // Get all blogs
    public List<BlogDTO> getAllBlogs() {
        try {
            return blogRepository.findAllBlogs().stream()
                .map(BlogMapper::toBlogDTO)
                .toList();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve all blogs from the database.");
        }
    }

    // Get a blog by ID
    public BlogDTO getBlogById(Long blogID) {
        try {
            BlogEntity blog = blogRepository.findBlogsById(blogID);
            if (blog == null) {
                throw new NotFoundException("Blog not found with ID: " + blogID);
            }
            return BlogMapper.toBlogDTO(blog);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve blog from the database. Blog ID: " + blogID);
        }
    }

    // ========================================| Creating Methods |========================================
    // Add a new blog
    // @Transactional
    public BlogDTO addBlog(BlogDTO blogDTO) {
        try {
            BlogEntity blog = BlogMapper.toBlogEntity(blogDTO);
            blog.setCreatedAt(ZonedDateTime.now());
            blog.setValidationStatus(false);
            blogRepository.persistBlog(blog);

            // Send blog for validation via Kafka
            blogValidationProducer.sendBlogForValidation(blog);

            return BlogMapper.toBlogDTO(blog);
        } catch (Exception e) {
            throw new DatabaseException("Failed to create new blog in the database.");
        }
    }

    // ========================================| Updating Methods |========================================
    // Update an existing blog
    @Transactional
    public BlogDTO updateBlog(Long blogID, BlogDTO blogDTO) {
        try {
            BlogEntity blog = blogRepository.findBlogsById(blogID);
            if (blog == null) {
                throw new NotFoundException("Blog not found with ID: " + blogID);
            }
            blog.setTitle(blogDTO.getTitle());
            blog.setContent(blogDTO.getContent());
            blog.setCreator(blogDTO.getCreator());
            blog.setLastChangedAt(ZonedDateTime.now());
            blog.setValidationStatus(false);
            blogRepository.updateBlog(blog);

            // Send blog for validation via Kafka
            blogValidationProducer.sendBlogForValidation(blog);
            
            return BlogMapper.toBlogDTO(blog);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to update blog in the database. Blog ID: " + blogID);
        }
    }

    // ========================================| Deleting Methods |========================================
    // Delete a blog by ID
    // @Transactional
    public void deleteBlog(Long blogID) {
        try {
            BlogEntity blog = blogRepository.findBlogsById(blogID);
            if (blog == null) {
                throw new NotFoundException("Blog not found with ID: " + blogID);
            }
            blogRepository.deleteBlogById(blogID);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete blog from the database. Blog ID: " + blogID);
        }
    }

    // Delete all blogs
    // @Transactional
    public void deleteAllBlogs() {
        try {
            blogRepository.deleteAllBlogs();
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete all blogs from the database.");
        }
    }

    // ========================================| Utility Methods |========================================
    // Count all blogs
    public Long countBlogs() {
        try {
            return blogRepository.countBlogs();
        } catch (Exception e) {
            throw new DatabaseException("Failed to count blogs in the database.");
        }
    }

    // ========================================| Validation Updates |========================================
    // Update the validation status of a blog
    @Transactional
    public void updateBlogValidationStatus(Long blogID, boolean isValidated) {
        BlogEntity blog = blogRepository.findBlogsById(blogID);
        if (blog == null) {
            throw new NotFoundException("Blog not found with ID: " + blogID);
        }
        try {
            blog.setValidationStatus(isValidated);
            blogRepository.updateBlog(blog);
        } catch (Exception e) {
            throw new DatabaseException("Failed to update validation status in the database. Blog ID: " + blogID);
        }
    }
}
