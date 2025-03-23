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
    
    // ------------------------------| Fetching |------------------------------
    // Get all blogs
    public List<BlogDTO> getAllBlogs() {
        try {
            return blogRepository.findAllBlogs().stream()
                .map(BlogMapper::toBlogDTO)
                .toList();
        } catch (Exception e) {
            throw new DatabaseException("Error fetching all blogs from the database.");
        }
    }

    // Get a blog by ID
    public BlogDTO getBlogById(Long blogID) {
        try {
            BlogEntity blog = blogRepository.findBlogsById(blogID);
            if (blog == null) {
                throw new NotFoundException("Blog with ID " + blogID + " not found.");
            }
            return BlogMapper.toBlogDTO(blog);
        } catch (NotFoundException e) {
            throw e; // Re-throw NotFoundException as it is not a database error
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while fetching blog with ID " + blogID + " from the database.");
        }
    }

    // ------------------------------| Creating |------------------------------
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
            throw new DatabaseException("An database error occured while adding a new blog to the database.");
        }
    }

    // ------------------------------| Updating |------------------------------
    // Update an existing blog
    @Transactional
    public BlogDTO updateBlog(Long blogID, BlogDTO blogDTO) {
        try {
            BlogEntity blog = blogRepository.findBlogsById(blogID);
            if (blog == null) {
                throw new NotFoundException("Blog with ID " + blogID + " not found.");
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
            throw new DatabaseException("An database error occured while updating blog with ID " + blogDTO.getBlogID() + " in the database.");
        }
    }

    // ------------------------------| Deleting |------------------------------
    // Delete a blog by ID
    // @Transactional
    public void deleteBlog(Long blogID) {
        try {
            BlogEntity blog = blogRepository.findBlogsById(blogID);
            if (blog == null) {
                throw new NotFoundException("Blog with ID " + blogID + " not found.");
            }
            blogRepository.deleteBlogById(blogID);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while deleting blog with ID " + blogID + " from the database.");
        }
    }

    // Delete all blogs
    // @Transactional
    public void deleteAllBlogs() {
        try {
            blogRepository.deleteAllBlogs();
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while deleting all blogs from the database.");
        }
    }

    // ------------------------------| Utility Methods |------------------------------
    // Count all blogs
    public Long countBlogs() {
        try {
            return blogRepository.countBlogs();
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while counting blogs in the database.");
        }
    }

    // ------------------------------| Validation Updates |------------------------------
    // Update the validation status of a blog
    @Transactional
    public void updateBlogValidationStatus(Long blogID, boolean isValidated) {
        BlogEntity blog = blogRepository.findBlogsById(blogID);
        if (blog == null) {
            throw new NotFoundException("Blog with ID " + blogID + " not found.");
        }
        try {
            blog.setValidationStatus(isValidated);
            blogRepository.updateBlog(blog);
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while updating the validation status of blog with ID " + blogID + " in the database.");
        }
    }
}
