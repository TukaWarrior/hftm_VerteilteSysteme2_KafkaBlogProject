package ch.hftm.blogproject.util;

import ch.hftm.blogproject.model.dto.BlogPostDTO;
import ch.hftm.blogproject.model.entity.BlogPost;

public class BlogPostMapper {

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
            }
        };
    }
}
