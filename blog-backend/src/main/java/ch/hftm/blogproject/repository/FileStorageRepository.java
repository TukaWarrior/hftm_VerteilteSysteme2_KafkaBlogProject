package ch.hftm.blogproject.repository;

import java.io.InputStream;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import ch.hftm.blogproject.model.entity.FileEntity;
import ch.hftm.blogproject.model.exception.StorageException;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.StreamingOutput;

@ApplicationScoped
public class FileStorageRepository {

    @ConfigProperty(name = "minio.bucket.name-files")
    String bucketName;
    @Inject
    MinioClient minioClient;

    // ========================================| Upload Methods |========================================
    public void uploadFile(FileUpload fileUpload, FileEntity fileMetadataEntity) {
        // ensureBucketExists();
        try (InputStream fileStream = fileUpload.uploadedFile().toFile().toPath().toUri().toURL().openStream()) {
            ensureBucketExists();
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileMetadataEntity.getFileName())
                    .stream(fileStream, fileMetadataEntity.getFileSize(), -1)
                    .contentType(fileMetadataEntity.getContentType())
                    .build()
            );
        } catch (Exception e) {
            throw new StorageException("Failed to upload file to storage bucket. Filename: " + fileMetadataEntity.getFileName());
        }
    }

    // ========================================| Delete Methods |========================================
    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build()
            );
        } catch (Exception e) {
            throw new StorageException("Failed to delete file from storage bucket. Filename: " + fileName);
        }
    }
    
    // ========================================| Download Methods |========================================
    public StreamingOutput createDownloadStream(String fileName) {
        return outputStream -> {
            try (InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build())) {
    
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (Exception e) {
                throw new StorageException("Failed to stream file from storage bucket. Filename: " + fileName);
            }
        };
    }

    // ========================================| Utility Methods |========================================
    public void ensureBucketExists() {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (bucketExists == false) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new StorageException("Failed to create storage bucket or verify its existance.");
        }
    }


    // ========================================| Alternatively: Stream based|========================================
    // These methods work with InputStreams directly, allowing the caller to manage stream creation/handling

    // public void uploadFileStream(InputStream fileStream, FileEntity fileMetadata) {
    //     try {
    //         ensureBucketExists();
    //         minioClient.putObject(
    //             PutObjectArgs.builder()
    //                 .bucket(bucketName)
    //                 .object(fileMetadata.getFileName())
    //                 .stream(fileStream, fileMetadata.getFileSize(), -1)
    //                 .contentType(fileMetadata.getContentType())
    //                 .build()
    //         );
    //     } catch (Exception e) {
    //         throw new StorageException("Failed to upload file to storage bucket. Filename: " + fileMetadata.getFileName());
    //     }
    // }

    // public InputStream downloadFileStream(FileEntity fileMetadata) {
    //     try {
    //         return minioClient.getObject(
    //             GetObjectArgs.builder()
    //                 .bucket(bucketName)
    //                 .object(fileMetadata.getFileName())
    //                 .build()
    //         );
    //     } catch (Exception e) {
    //         throw new StorageException("Failed to retrieve file from storage bucket. Filename: " + fileMetadata.getFileName());
    //     }
    // }
}
