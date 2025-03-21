package ch.hftm.blogproject.control;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import io.minio.GetObjectArgs;
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

    private static final String BUCKET_NAME = "files"; // Replace with your bucket name

    // Upload a file to MinIO
    public void uploadFile(String storageKey, InputStream fileStream, long fileSize, String contentType) throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException {
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(storageKey) // The key used to store the file in MinIO
                    .stream(fileStream, fileSize, -1) // InputStream, file size, and part size (-1 for unknown)
                    .contentType(contentType) // MIME type of the file
                    .build()
            );
        } catch (MinioException e) {
            throw new RuntimeException("Error uploading file to MinIO: " + e.getMessage(), e);
        }
    }

    // Retrieve a file from MinIO
    public InputStream getFile(String storageKey) throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(storageKey) // The key used to retrieve the file
                    .build()
            );
        } catch (MinioException e) {
            throw new RuntimeException("Error retrieving file from MinIO: " + e.getMessage(), e);
        }
    }

    // Delete a file from MinIO
    public void deleteFile(String storageKey) throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(storageKey) // The key used to delete the file
                    .build()
            );
        } catch (MinioException e) {
            throw new RuntimeException("Error deleting file from MinIO: " + e.getMessage(), e);
        }
    }
}