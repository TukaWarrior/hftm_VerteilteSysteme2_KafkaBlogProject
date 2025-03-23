package ch.hftm.blogproject.BasicExample;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExampleFileRepository implements PanacheRepository<ExampleFile> {
}