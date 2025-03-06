package ch.hftm.blogproject.control;

import java.time.ZonedDateTime;
import java.util.List;
import ch.hftm.blogproject.model.dto.BlogDTO;
import ch.hftm.blogproject.model.entity.Blog;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.repository.BlogRepository;
import ch.hftm.blogproject.util.BlogMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BlogService {

    @Inject
    BlogRepository blogRepository;

    // Get all blogs
    public List<BlogDTO> getAllBlogs() {
        return blogRepository.findAllBlogs().stream()
            .map(BlogMapper::toBlogDTO)
            .toList();
    }

    // Get a blog by ID
    public BlogDTO getBlogById(Long blogID) {
        Blog blog = blogRepository.findBlogsById(blogID);
        if (blog == null) {
            throw new NotFoundException("Blog with ID " + blogID + " not found.");
        }
        return BlogMapper.toBlogDTO(blog);
    }

    // Add a new blog
    public BlogDTO addBlog(BlogDTO blogDTO) {
        Blog blog = BlogMapper.toBlogEntity(blogDTO);
        blog.setCreatedAt(ZonedDateTime.now());
        blog.setValidated(true); // Assume validation is always true for now
        blogRepository.persistBlog(blog);
        return BlogMapper.toBlogDTO(blog);
    }

    // Update an existing blog
    public BlogDTO updateBlog(BlogDTO blogDTO) {
        Blog blog = blogRepository.findBlogsById(blogDTO.getBlogID());
        if (blog == null) {
            throw new NotFoundException("Blog with ID " + blogDTO.getBlogID() + " not found.");
        }
        blog.setTitle(blogDTO.getTitle());
        blog.setContent(blogDTO.getContent());
        blog.setCreator(blogDTO.getCreator());
        blog.setLastChangedAt(ZonedDateTime.now());
        blogRepository.updateBlog(blog);
        return BlogMapper.toBlogDTO(blog);
    }

    // Delete a blog by ID
    public void deleteBlog(Long blogID) {
        Blog blog = blogRepository.findBlogsById(blogID);
        if (blog == null) {
            throw new NotFoundException("Blog with ID " + blogID + " not found.");
        }
        blogRepository.deleteBlogById(blogID);
    }

    // Delete all blogs
    public void deleteAllBlogs() {
        blogRepository.deleteAllBlogs();
    }

    // Count all blogs
    public Long countBlogs() {
        return blogRepository.countBlogs();
    }
}