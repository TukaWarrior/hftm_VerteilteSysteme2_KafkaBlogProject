package ch.hftm.blogproject.control;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.jboss.resteasy.reactive.multipart.FileDownload;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import ch.hftm.blogproject.model.entity.FileMetadataEntity;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.StreamingOutput;

@ApplicationScoped
public class FileMinioService {

    @Inject
    MinioClient minioClient;

    private static final String BUCKET_NAME = "files";

    public void uploadFile(FileUpload fileUpload, FileMetadataEntity fileMetadataEntity) {
        // ensureBucketExists();
        try (InputStream fileStream = fileUpload.uploadedFile().toFile().toPath().toUri().toURL().openStream()) {
            ensureBucketExists();
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(fileMetadataEntity.getFileName())
                    .stream(fileStream, fileMetadataEntity.getFileSize(), -1)
                    .contentType(fileMetadataEntity.getContentType())
                    .build()
            );
        } catch (MinioException e) {
            throw new RuntimeException("Error uploading file to MinIO: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error processing the uploaded file: " + e.getMessage(), e);
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
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException | ServerException | XmlParserException e) {
            throw new RuntimeException("Error deleting file from MinIO");
        }
    }

// ========================================| Stream Based |========================================
    public void uploadFileStream(InputStream fileStream, FileMetadataEntity fileMetadata) {
        try {
            ensureBucketExists();
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(fileMetadata.getFileName())
                    .stream(fileStream, fileMetadata.getFileSize(), -1)
                    .contentType(fileMetadata.getContentType())
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file stream to MinIO: " + e.getMessage(), e);
        }
    }

    public InputStream downloadFileStream(FileMetadataEntity fileMetadata) throws IOException {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(fileMetadata.getFileName())
                    .build()
            );
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new IOException("Error downloading file from MinIO: " + e.getMessage(), e);
        }
    }
    
    
    public StreamingOutput createDownloadStream(String fileName) {
        return outputStream -> {
            try (InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(fileName)
                        .build())) {
    
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (Exception e) {
                throw new IOException("Error streaming file from MinIO: " + e.getMessage(), e);
            }
        };
    }
// ========================================| File Container Based |========================================
    























    public void ensureBucketExists() throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            }
        } catch (MinioException e) {
            throw new RuntimeException("Error ensuring bucket exists: " + e.getMessage(), e);
        } catch (IOException | IllegalArgumentException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error processing the uploaded file: " + e.getMessage(), e);
        }
    }
}


    // public InputStream getFile(String storageKey) throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException {
    //     try {
    //         return minioClient.getObject(
    //             GetObjectArgs.builder()
    //                 .bucket(BUCKET_NAME)
    //                 .object(storageKey)
    //                 .build()
    //         );
    //     } catch (MinioException e) {
    //         throw new RuntimeException("Error retrieving file from MinIO: " + e.getMessage(), e);
    //     }
    // }

    
