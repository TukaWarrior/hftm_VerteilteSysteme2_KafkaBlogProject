package ch.hftm.blogproject.control;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import ch.hftm.blogproject.model.entity.ExampleImage;
import ch.hftm.blogproject.repository.ImageRepository;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ExampleImageService {
    
    @Inject
    MinioClient minioClient;

    @Inject
    ImageRepository imageRepository;

    private static final String BUCKET_NAME = "test-images"; 

    @Transactional
    public ExampleImage uploadImage(String fileName, InputStream fileStream, long fileSize, String contentType) {
        try (InputStream stream = fileStream) { // Ensure the InputStream is closed
            // Upload the file to MinIO
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(fileName)
                    .stream(stream, fileSize, -1)
                    .contentType(contentType)
                    .build()
            );

            // Save metadata in the database
            ExampleImage image = new ExampleImage(fileName, contentType);
            imageRepository.persist(image);

            return image;
        } catch (MinioException e) {
            throw new RuntimeException("Error uploading image to MinIO: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error processing the uploaded file: " + e.getMessage(), e);
        }
    }

    public InputStream downloadImage(String fileName) throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException {
        try {
            // Retrieve the file from MinIO
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(fileName)
                    .build()
            );
        } catch (MinioException e) {
            throw new RuntimeException("Error retrieving image from MinIO: " + e.getMessage(), e);
        }
    }

    
    @PostConstruct
    public void initializeBucket() {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing MinIO bucket: " + e.getMessage(), e);
        }
    }
}
