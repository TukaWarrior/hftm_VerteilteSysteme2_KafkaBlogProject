package ch.hftm.blogproject.util;

import java.time.ZonedDateTime;

import ch.hftm.blogproject.model.dto.BlogDTO;
import ch.hftm.blogproject.model.entity.BlogEntity;

public class BlogMapper {

    public static BlogEntity toBlogEntity(BlogDTO blogDTO) {
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setTitle(blogDTO.getTitle());
        blogEntity.setContent(blogDTO.getContent());
        blogEntity.setCreator(blogDTO.getCreator());
        blogEntity.setCreatedAt(blogDTO.getCreatedAt());
        blogEntity.setLastChangedAt(ZonedDateTime.now());
        return blogEntity;
    }

    public static BlogDTO toBlogDTO(BlogEntity blogEntity) {
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setBlogID(blogEntity.getBlogID());
        blogDTO.setTitle(blogEntity.getTitle());
        blogDTO.setContent(blogEntity.getContent());
        blogDTO.setCreator(blogEntity.getCreator());
        blogDTO.setCreatedAt(blogEntity.getCreatedAt());
        blogDTO.setLastChangedAt(blogEntity.getLastChangedAt());
        blogDTO.setValidationStatus(blogEntity.getValidationStatus());
        return blogDTO;
    }
}
