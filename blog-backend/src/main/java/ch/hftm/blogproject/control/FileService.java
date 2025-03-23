package ch.hftm.blogproject.control;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import ch.hftm.blogproject.model.dto.FileMetadataDTO;
import ch.hftm.blogproject.model.entity.FileMetadataEntity;
import ch.hftm.blogproject.model.exception.DatabaseException;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.repository.FileMetadataRepository;
import ch.hftm.blogproject.util.FileMetadataMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FileService {

    @Inject
    FileMetadataRepository fileRepository;
    @Inject
    FileMinioService fileMinioService;
    @Inject
    FileMetadataMapper fileMetadataMapper;

    // ------------------------------| Create Methods |------------------------------

    @Transactional
    public FileMetadataDTO uploadFile(FileUpload fileUpload) {
        try {
            FileMetadataEntity fileMetadataEntity = new FileMetadataEntity();
            fileMetadataEntity.setFileName(UUID.randomUUID().toString() + "_" + fileUpload.fileName());
            fileMetadataEntity.setContentType(fileUpload.contentType());
            fileMetadataEntity.setFileSize(fileUpload.size());
            fileMetadataEntity.setUploadDate(ZonedDateTime.now());
            // Saves the File entity and returns the entity with the id set by the database.
            fileRepository.persistFile(fileMetadataEntity);
            // Upload the file to MinIO
            fileMinioService.uploadFile(fileUpload, fileMetadataEntity);
            return fileMetadataMapper.toFileMetadataDTO(fileMetadataEntity);
        } catch (Exception e) {
            throw new DatabaseException("Failed to upload file to the database.");
        }
    }

    // ------------------------------| Retrieving Methods |------------------------------
    public List<FileMetadataDTO> getAllFiles() {
        try {
            return fileRepository.findAllFiles().stream()
                .map(fileMetadataMapper::toFileMetadataDTO)
                .toList();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve all files from the database.");
        }
    }

    public FileMetadataDTO getFileMetadataById(Long id) {
        try {
            FileMetadataEntity fileMetadataEntity = fileRepository.findFileById(id);
            if (fileMetadataEntity == null) {
                throw new NotFoundException("File not found with ID: " + id);
            }
            return fileMetadataMapper.toFileMetadataDTO(fileMetadataEntity);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve file from the database. File ID: " + id);
        }
    }

    public FileMetadataDTO getFileMetadataByFileName(String fileName) {
        try {
            FileMetadataEntity fileMetadataEntity = fileRepository.findFileByFileName(fileName);
            if (fileMetadataEntity == null) {
                throw new NotFoundException("File not found with filename: " + fileName);
            }
            return fileMetadataMapper.toFileMetadataDTO(fileMetadataEntity);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve file from the database. Filename: " + fileName);
        }
    }

    // ------------------------------| Deleting Methods |------------------------------
    public void deleteFile(Long fileId) {
        try {
            FileMetadataEntity fileMetadataEntity = fileRepository.findFileById(fileId);
            if (fileMetadataEntity == null) {
                throw new NotFoundException("File not found with ID: " + fileId);
            }
            fileMinioService.deleteFile(fileMetadataEntity.getFileName());
            fileRepository.deleteFileById(fileId);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete file from the database. File ID: " + fileId);
        }
    }

    public FileMetadataDTO downloadFile(Long id) {
        try {
            FileMetadataEntity fileMetadataEntity = fileRepository.findFileById(id);
            if (fileMetadataEntity == null) {
                throw new NotFoundException("File not found with ID: " + id);
            }
            // Convert to DTO
            FileMetadataDTO fileMetadataDTO = fileMetadataMapper.toFileMetadataDTO(fileMetadataEntity);

            // Add streaming output
            fileMetadataDTO.setDownloadStream(
                fileMinioService.createDownloadStream(fileMetadataEntity.getFileName())
            );

            // fileMinioService.downloadFile(fileMetadataEntity.getFileName());
            return fileMetadataDTO;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to prepare file for download. File ID: " + id);
        }
    }

    // ------------------------------| Utility Methods |------------------------------
    // Count all files
    public Long countFiles() {
        try {
            return fileRepository.countFiles();
        } catch (Exception e) {
            throw new DatabaseException("Failed to count files in the database.");
        }
    }
}