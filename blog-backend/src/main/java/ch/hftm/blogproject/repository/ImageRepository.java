package ch.hftm.blogproject.repository;

import ch.hftm.blogproject.model.entity.ExampleImage;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImageRepository implements PanacheRepository<ExampleImage> {
}