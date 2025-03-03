package ch.hftm.blogproject.control;

import java.time.ZonedDateTime;

import ch.hftm.blogproject.model.dto.BlogPostDTO;
import ch.hftm.blogproject.model.entity.BlogPost;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.repository.BlogPostRepository;
import ch.hftm.blogproject.util.DTOConverter;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

@ApplicationScoped
public class BlogPostService {

    @Inject
    BlogPostRepository blogPostRepository;

    public Multi<BlogPostDTO> getBlogPosts() {
        return blogPostRepository.findAll().stream().onItem().transform(DTOConverter::toBlogPostDto);
    }

    public Uni<BlogPostDTO> getBlogPostById(Long blogPostID) {
        return blogPostRepository.findById(blogPostID).onItem().failWith(() -> new NotFoundException("Blog post with ID " + blogPostID + " not found."))
                .onItem().transform(DTOConverter::toBlogPostDto);
    }

    public Uni<BlogPostDTO> addBlogPost(BlogPostDTO blogPostDTO) {
        BlogPost blogPost = new BlogPost();
        blogPost.setTitle(blogPostDTO.getTitle());
        blogPost.setContent(blogPostDTO.getContent());
        blogPost.setCreator(blogPostDTO.getCreator());
        blogPost.setCreatedAt(ZonedDateTime.now());
        return blogPostRepository.persist(blogPost).onItem().transform(DTOConverter::toBlogPostDto);
    }

    // ------------------------- Getting BlogPosts -------------------------
    // Get all BlogPosts with search and pagination
    // public List<BlogPostDTO> OLDgetBlogPosts(Optional<String> searchString, Optional<Integer> page) {
    //     PanacheQuery<BlogPost> blogPostQuery;
    //     try {
    //         if (searchString.isEmpty()) {
    //             blogPostQuery = blogPostRepository.findAll();
    //         } else {
    //             blogPostQuery = blogPostRepository.find("title like ?1 or content like ?1", "%" + searchString.get() + "%");
    //         } 
    //         int pageNumber = page.orElse(0);
    //         List<BlogPost> blogposts = blogPostQuery.page(Page.of(pageNumber, 10)).list();
    //         return DTOConverter.toBlogPostDtoList(blogposts);
    //     } catch (Exception e) {
    //         throw new DatabaseException("Error while accessing the database.", e);
    //     }
    // }

    // Get all Blog Posts with Sorting
    // public List<BlogPostDTO> OLDgetAllBlogPostsSorted(String sortBy, boolean ascending) {
    //     try {
    //         Sort sort = ascending ? Sort.ascending(sortBy) : Sort.descending(sortBy);
    //         List<BlogPost> blogPosts = blogPostRepository.findAll(sort).list();
    //         return DTOConverter.toBlogPostDtoList(blogPosts);
    //     } catch (Exception e) {
    //         throw new DatabaseException("Error while accessing the database.", e);
    //     }
    // }

    // Get a BlogPost by ID
    // public BlogPostDTO OLDgetBlogPostById(Long blogPostID) {
    //     BlogPost blogPost;
    //     try {
    //         blogPost = blogPostRepository.findById(blogPostID);
    //         if (blogPost == null) {
    //             throw new NotFoundException("Blog post with ID " + blogPostID + " not found.");
    //         }
    //         return DTOConverter.toBlogPostDto(blogPost);
    //     } catch (Exception e) {
    //         throw new DatabaseException("Error while accessing the database.", e);
    //     }
    // }

    // Get Blog Posts by Creator
    // public List<BlogPostDTO> OLDgetBlogPostsByCreator(String creator) {
    //     try {
    //         List<BlogPost> blogPosts = blogPostRepository.find("creator", creator).list();
    //         return DTOConverter.toBlogPostDtoList(blogPosts);
    //     } catch (Exception e) {
    //         throw new DatabaseException("Error while accessing the database.", e);
    //     }
    // }

    // public List<BlogPostDTO> OLDgetBlogPostsByDateRange(ZonedDateTime startDate, ZonedDateTime endDate) {
    //     try {
    //         List<BlogPost> blogPosts = blogPostRepository
    //             .find("createdAt BETWEEN ?1 AND ?2", startDate, endDate).list();
    //         return DTOConverter.toBlogPostDtoList(blogPosts);
    //     } catch (Exception e) {
    //         throw new DatabaseException("Error while accessing the database.", e);
    //     }
    // }

    // Add a new BlogPost
    // @Transactional
    // public BlogPostDTO OLDaddBlogPost(BlogPostDTO blogPostDTO) {
    //     BlogPost blogPost = new BlogPost();
    //     blogPost.setTitle(blogPostDTO.getTitle());
    //     blogPost.setContent(blogPostDTO.getContent());
    //     blogPost.setCreator(blogPostDTO.getCreator());
    //     blogPost.setCreatedAt(ZonedDateTime.now());
    //     try {
    //         blogPostRepository.persist(blogPost);
    //     } catch (Exception e) {
    //         throw new DatabaseException("Error while adding the blog post to the database.", e);
    //     }
    //     return DTOConverter.toBlogPostDto(blogPost);
    // }

    // Update blogPost
    // @Transactional
    // public BlogPostDTO OLDputBlogPost(BlogPostDTO blogPostDTO) {
    //     BlogPost existingBlogPost;
    //     try {
    //         existingBlogPost = blogPostRepository.findById(blogPostDTO.getBlogPostID());
    //     } catch (Exception e) {
    //         throw new DatabaseException("Error while accessing the database.", e);
    //     }
    //     if (existingBlogPost == null) {
    //         throw new NotFoundException("Blog post with ID " + blogPostDTO.getBlogPostID() + " not found.");
    //     }
    //     existingBlogPost.setTitle(blogPostDTO.getTitle());
    //     existingBlogPost.setContent(blogPostDTO.getContent());
    //     existingBlogPost.setLastChangedAt(ZonedDateTime.now());

    //     try {
    //         blogPostRepository.persist(existingBlogPost);
    //     } catch (Exception e) {
    //         throw new DatabaseException("Error while updating the blog post with ID " + blogPostDTO.getBlogPostID(), e);
    //     }
    //     return DTOConverter.toBlogPostDto(existingBlogPost);
    // }

    // @Transactional
    // public BlogPostDTO OLDpatchBlogPost(Long id, BlogPostDTO blogPostDTO) {
    //     BlogPost existingBlogPost = blogPostRepository.findById(id);
    //     if (existingBlogPost == null) {
    //         throw new NotFoundException("BlogPost not found for id: " + id);
    //     }
    //     // Update only the fields that are provided in the request (patch-like behavior)
    //     if (blogPostDTO.getTitle() != null) {
    //         existingBlogPost.setTitle(blogPostDTO.getTitle());
    //     }
    //     if (blogPostDTO.getContent() != null) {
    //         existingBlogPost.setContent(blogPostDTO.getContent());
    //     }
    //     if (blogPostDTO.getCreator() != null) {
    //         existingBlogPost.setCreator(blogPostDTO.getCreator());
    //     }
    //     existingBlogPost.setLastChangedAt(ZonedDateTime.now());
    //     try {
    //         blogPostRepository.persist(existingBlogPost);
    //     } catch (Exception e) {
    //         throw new DatabaseException("Error while partially updating the blog post with ID " + id, e);
    //     }
    //     return DTOConverter.toBlogPostDto(existingBlogPost);
    // }

    // Delete a BlogPost by id
    // @Transactional
    // public BlogPostDTO OLDdeleteBlogPost (Long blogPostID) {

    //     BlogPost blogPost = blogPostRepository.findById(blogPostID);
    //     if (blogPost == null) {
    //         throw new NotFoundException("Blog post with ID " + blogPostID + " not found.");
    //     }
    //     BlogPostDTO deletedBlogPostDTO = DTOConverter.toBlogPostDto(blogPost);
    //     try {
    //         blogPostRepository.deleteById(blogPostID);
    //     } catch (Exception e) {
    //         throw new DatabaseException("Error while deleting the blog post with ID " + blogPostID, e);
    //     }
    //     return deletedBlogPostDTO;
    // }

    // Delete all BlogPosts
    // @Transactional
    // public void OLDdeleteAllBlogPosts() {
    //     try {
    //         blogPostRepository.deleteAll();
    //     } catch (Exception e) {
    //         throw new DatabaseException("Error while deleting all blog posts.", e);
    //     }
    // }

    // ------------------------- Counting -------------------------
    // Count all BlogPosts
    // public Long OLDcountBlogPosts() {
    //     try {
    //         return blogPostRepository.count();
    //     } catch (Exception e) {
    //         throw new DatabaseException("Error while counting blog posts in the database.", e);
    //     }
    // }
}
