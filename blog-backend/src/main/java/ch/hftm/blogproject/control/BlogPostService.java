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

@ApplicationScoped
public class BlogPostService {

    @Inject
    BlogPostRepository blogPostRepository;

    // Get all BlogPosts
    public Multi<BlogPostDTO> getBlogPosts() {
        return blogPostRepository.findAllBlogPosts()
                .onItem().transform(DTOConverter::toBlogPostDto);
    }

    // Get BlogPost by ID
    public Uni<BlogPostDTO> getBlogPostById(Long blogPostID) {
        return blogPostRepository.findBlogPostById(blogPostID)
                .onItem().ifNull().failWith(() -> new NotFoundException("Blog post with ID " + blogPostID + " not found."))
                .onItem().transform(DTOConverter::toBlogPostDto);
    }

    // Add a new BlogPost
    public Uni<BlogPostDTO> addBlogPost(BlogPostDTO blogPostDTO) {
        BlogPost blogPost = DTOConverter.toBlogPost(blogPostDTO);
        blogPost.setCreatedAt(ZonedDateTime.now());
        return blogPostRepository.persistBlogPost(blogPost)
                .onItem().transform(DTOConverter::toBlogPostDto);
    }

    // Update a BlogPost
    public Uni<BlogPostDTO> updateBlogPost(BlogPostDTO blogPostDTO) {
        BlogPost blogPost = DTOConverter.toBlogPost(blogPostDTO);
        blogPost.setLastChangedAt(ZonedDateTime.now());
        return blogPostRepository.updateBlogPost(blogPost)
                .onItem().ifNull().failWith(() -> new NotFoundException("Blog post with ID " + blogPostDTO.getBlogPostID() + " not found."))
                .onItem().transform(DTOConverter::toBlogPostDto);
    }

    // Delete a BlogPost by ID
    public Uni<Void> deleteBlogPost(Long blogPostID) {
        return blogPostRepository.deleteBlogPostById(blogPostID)
                .onItem().ifNull().failWith(() -> new NotFoundException("Blog post with ID " + blogPostID + " not found."))
                .replaceWithVoid();
    }

    // Delete all BlogPosts
    public Uni<Void> deleteAllBlogPosts() {
        return blogPostRepository.deleteAllBlogPosts();
    }

    // Count all BlogPosts
    public Uni<Long> countBlogPosts() {
        return blogPostRepository.countBlogPosts();
    }

    // Get BlogPosts by Creator
    public Multi<BlogPostDTO> getBlogPostsByCreator(String creator) {
        return blogPostRepository.findBlogPostsByCreator(creator)
                .onItem().transform(DTOConverter::toBlogPostDto);
    }

    // Get BlogPosts by Date Range
    public Multi<BlogPostDTO> getBlogPostsByDateRange(ZonedDateTime startDate, ZonedDateTime endDate) {
        return blogPostRepository.findBlogPostsByDateRange(startDate, endDate)
                .onItem().transform(DTOConverter::toBlogPostDto);
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
}
