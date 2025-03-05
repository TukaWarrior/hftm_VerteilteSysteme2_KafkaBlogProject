package ch.hftm.blogproject.util;

import java.time.ZonedDateTime;

import ch.hftm.blogproject.model.dto.BlogDTO;
import ch.hftm.blogproject.model.entity.Blog;

public class BlogMapper {

    public static Blog toBlogEntity(BlogDTO blogDTO) {
        Blog blogEntity = new Blog();
        blogEntity.setTitle(blogDTO.getTitle());
        blogEntity.setContent(blogDTO.getContent());
        blogEntity.setCreator(blogDTO.getCreator());
        blogEntity.setCreatedAt(blogDTO.getCreatedAt());
        blogEntity.setLastChangedAt(ZonedDateTime.now());
        return blogEntity;
    }

    public static BlogDTO toBlogDTO(Blog blogEntity) {
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setBlogID(blogEntity.getBlogID());
        blogDTO.setTitle(blogEntity.getTitle());
        blogDTO.setContent(blogEntity.getContent());
        blogDTO.setCreator(blogEntity.getCreator());
        blogDTO.setCreatedAt(blogEntity.getCreatedAt());
        blogDTO.setLastChangedAt(blogEntity.getLastChangedAt());
        return blogDTO;
    }
}
