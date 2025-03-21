package ch.hftm.blogproject.control;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import ch.hftm.blogproject.model.entity.File;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FileMinioService {

    @Inject
    MinioClient minioClient;

    private static final String BUCKET_NAME = "files";

    public void uploadFile(File file, FileUpload fileUpload) throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException {
        ensureBucketExists();
        try (InputStream fileStream = fileUpload.uploadedFile().toFile().toPath().toUri().toURL().openStream()) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(file.getId().toString())
                    .stream(fileStream, file.getFileSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
        } catch (MinioException e) {
            throw new RuntimeException("Error uploading file to MinIO: " + e.getMessage(), e);
        }
    }

    public InputStream getFile(String storageKey) throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(storageKey)
                    .build()
            );
        } catch (MinioException e) {
            throw new RuntimeException("Error retrieving file from MinIO: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String storageKey) throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(storageKey)
                    .build()
            );
        } catch (MinioException e) {
            throw new RuntimeException("Error deleting file from MinIO: " + e.getMessage(), e);
        }
    }








    public void ensureBucketExists() throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            }
        } catch (MinioException e) {
            throw new RuntimeException("Error ensuring bucket exists: " + e.getMessage(), e);
        }
    }
    
}