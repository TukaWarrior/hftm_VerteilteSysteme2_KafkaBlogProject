package ch.hftm.blogproject.control;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import ch.hftm.blogproject.model.dto.FileDTO;
import ch.hftm.blogproject.model.entity.File;
import ch.hftm.blogproject.model.exception.DatabaseException;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.repository.FileRepository;
import ch.hftm.blogproject.util.FileUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
@ApplicationScoped
public class FileService {

    @Inject
    FileRepository fileRepository;

    @Inject
    FileMinioService fileMinioService;

    @Inject
    FileUtils fileUtils;

    @Transactional
    public void uploadFile(FileUpload fileUpload) {
        try {
            File file = new File();
            file.setFileName(fileUpload.fileName());
            file.setContentType(fileUpload.contentType());
            file.setFileSize(fileUpload.size());
            file.setUploadDate(ZonedDateTime.now());

            // Saves the File entity and returns the entity with the id set by the database.
            fileRepository.saveFile(file);

            // Upload the file to MinIO
            fileMinioService.uploadFile(file, fileUpload);
        } catch (Exception e) {
            throw new DatabaseException("Failed to upload file: " + e.getMessage());
        }
    }

    public FileDTO getFileWithData(Long id) {
        File file = fileRepository.findFileById(id);
        if (file == null) {
            throw new NotFoundException("File with ID " + id + " not found.");
        }

        try (InputStream fileStream = fileMinioService.getFile(file.getStorageKey())) {
            byte[] fileData = fileStream.readAllBytes();
            return FileMapperOld.toFileDTOWithFileData(file, fileData);
        } catch (Exception e) {
            throw new DatabaseException("Failed to retrieve file data: " + e.getMessage());
        }
    }

    public FileDTO getFileMetadata(Long id) {
        File file = fileRepository.findFileById(id);
        if (file == null) {
            throw new NotFoundException("File with ID " + id + " not found.");
        }
        return FileMapperOld.toFileDTO(file);
    }

    @Transactional
    public void deleteFile(Long id) throws InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException, IOException {
        File file = fileRepository.findFileById(id);
        if (file == null) {
            throw new NotFoundException("File with ID " + id + " not found.");
        }

        fileMinioService.deleteFile(file.getStorageKey());
        fileRepository.deleteFileById(id);
    }

    // ------------------------------| Utility Methods |------------------------------
    // Count all files
    public long countFiles() {
        try {
            return fileRepository.countFiles();
        } catch (Exception e) {
            throw new DatabaseException("An error occurred while counting files in the database.");
        }
    }
}