package ch.hftm.blogproject.repository;

import ch.hftm.blogproject.model.entity.BlogPost;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

// This class serves as the interface between the BlogService class and the MySQL Database.

@ApplicationScoped
public class BlogPostRepository implements PanacheRepository<BlogPost>{
}
