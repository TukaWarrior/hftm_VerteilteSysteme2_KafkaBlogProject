package ch.hftm.blogproject.control;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.multipart.FileDownload;
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
            throw new DatabaseException("Failed to upload file: " + e.getMessage());
        }
    }

    // ------------------------------| Retrieving Methods |------------------------------
    public List<FileMetadataDTO> getAllFiles() {
        try {
            return fileRepository.findAllFiles().stream()
                .map(fileMetadataMapper::toFileMetadataDTO)
                .toList();
        } catch (Exception e) {
            throw new DatabaseException("Failed to upload file: " + e.getMessage());
        }
    }

    public FileMetadataDTO getFileMetadataById(Long id) {
        try {
            FileMetadataEntity fileMetadataEntity = fileRepository.findFileById(id);
            if (fileMetadataEntity == null) {
                throw new NotFoundException("File with id " + id + " not found.");
            }
            return fileMetadataMapper.toFileMetadataDTO(fileMetadataEntity);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while fetching file with ID " + id + " from the database.");
        }
    }

    public FileMetadataDTO getFileMetadataByFileName(String fileName) {
        try {
            FileMetadataEntity fileMetadataEntity = fileRepository.findFileByFileName(fileName);
            if (fileMetadataEntity == null) {
                throw new NotFoundException("File with filename " + fileName + " not found.");
            }
            return fileMetadataMapper.toFileMetadataDTO(fileMetadataEntity);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while fetching file with filename " + fileName + " from the database.");
        }
    }

    // ------------------------------| Deleting Methods |------------------------------
    public void deleteFile(Long fileId) {
        try {
            FileMetadataEntity fileMetadataEntity = fileRepository.findFileById(fileId);
            if (fileMetadataEntity == null) {
                throw new NotFoundException("File with id " + fileId + " not found.");
            }
            fileMinioService.deleteFile(fileMetadataEntity.getFileName());
            fileRepository.deleteFileById(fileId);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while deleting file with ID " + fileId + " from the database.");
        }
    }

    public FileMetadataDTO downloadFile(Long id) {
        try {
            FileMetadataEntity fileMetadataEntity = fileRepository.findFileById(id);
            if (fileMetadataEntity == null) {
                throw new NotFoundException("File with ID " + id + " not found.");
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
            throw new DatabaseException("Failed to prepare file for download: " + e.getMessage());
        }
    }
        

    //     try (InputStream fileStream = fileMinioService.getFile(file.getStorageKey())) {
    //         byte[] fileData = fileStream.readAllBytes();
    //         return fileMapper.toFileDTOWithFileData(file, fileData);
    //     } catch (Exception e) {
    //         throw new RuntimeException("Failed to retrieve file data.");
    //     }
    // }


    // ------------------------------| Utility Methods |------------------------------
    // Count all files
    public Long countFiles() {
        try {
            return fileRepository.countFiles();
        } catch (Exception e) {
            throw new DatabaseException("An error occurred while counting files in the database.");
        }
    }
}