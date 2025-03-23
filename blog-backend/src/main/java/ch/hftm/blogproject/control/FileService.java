package ch.hftm.blogproject.control;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import ch.hftm.blogproject.model.dto.FileDTO;
import ch.hftm.blogproject.model.entity.FileEntity;
import ch.hftm.blogproject.model.exception.DatabaseException;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.repository.FileRepository;
import ch.hftm.blogproject.repository.FileStorageRepository;
import ch.hftm.blogproject.util.FileMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FileService {

    @Inject
    FileRepository fileRepository;
    @Inject
    FileStorageRepository fileStorageRepository;
    @Inject
    FileMapper fileMapper;

    // ------------------------------| Create Methods |------------------------------

    @Transactional
    public FileDTO uploadFile(FileUpload fileUpload) {
        try {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(UUID.randomUUID().toString() + "_" + fileUpload.fileName());
            fileEntity.setContentType(fileUpload.contentType());
            fileEntity.setFileSize(fileUpload.size());
            fileEntity.setUploadDate(ZonedDateTime.now());
            // Saves the File entity and returns the entity with the id set by the database.
            fileRepository.persistFile(fileEntity);
            // Upload the file to MinIO
            fileStorageRepository.uploadFile(fileUpload, fileEntity);
            return fileMapper.toFileDTO(fileEntity);
        } catch (Exception e) {
            throw new DatabaseException("Failed to upload file to the database.");
        }
    }

    // ------------------------------| Retrieving Methods |------------------------------
    public List<FileDTO> getAllFilesMetadata() {
        try {
            return fileRepository.findAllFiles().stream()
                .map(fileMapper::toFileDTO)
                .toList();
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve all files from the database.");
        }
    }

    public FileDTO getFileMetadataById(Long id) {
        try {
            FileEntity fileEntity = fileRepository.findFileById(id);
            if (fileEntity == null) {
                throw new NotFoundException("File not found with ID: " + id);
            }
            return fileMapper.toFileDTO(fileEntity);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve file from the database. File ID: " + id);
        }
    }

    public FileDTO getFileMetadataByFileName(String fileName) {
        try {
            FileEntity fileEntity = fileRepository.findFileByFileName(fileName);
            if (fileEntity == null) {
                throw new NotFoundException("File not found with filename: " + fileName);
            }
            return fileMapper.toFileDTO(fileEntity);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve file from the database. Filename: " + fileName);
        }
    }

    public FileDTO downloadFile(Long id) {
        try {
            FileEntity fileEntity = fileRepository.findFileById(id);
            if (fileEntity == null) {
                throw new NotFoundException("File not found with ID: " + id);
            }
            FileDTO fileDTO = fileMapper.toFileDTO(fileEntity);
            fileDTO.setDownloadStream(fileStorageRepository.createDownloadStream(fileEntity.getFileName()));
            return fileDTO;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to prepare file for download. File ID: " + id);
        }
    }

    // ------------------------------| Deleting Methods |------------------------------
    public void deleteFile(Long id) {
        try {
            FileEntity fileEntity = fileRepository.findFileById(id);
            if (fileEntity == null) {
                throw new NotFoundException("File not found with ID: " + id);
            }
            fileStorageRepository.deleteFile(fileEntity.getFileName());
            fileRepository.deleteFileById(id);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete file from the database. File ID: " + id);
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