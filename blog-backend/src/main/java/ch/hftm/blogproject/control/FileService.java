package ch.hftm.blogproject.control;

import java.time.ZonedDateTime;

import ch.hftm.blogproject.model.dto.FileDTO;
import ch.hftm.blogproject.model.entity.File;
import ch.hftm.blogproject.model.exception.DatabaseException;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.repository.FileRepository;
import ch.hftm.blogproject.util.FileMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FileService {

    @Inject
    FileRepository fileRepository;

    // ------------------------------| Fetching |------------------------------
    // Get a file by filename
    public FileDTO getFileById(Long id) {
        try {
            File file = fileRepository.findFileById(id);
            if (file == null) {
                throw new NotFoundException("File with ID " + id + " not found.");
            }
            return FileMapper.toFileDTO(file);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while fetching file with ID " + id + " from the database.");
        }
    }

    // Get a file by filename
    public FileDTO getFileByFilename(String filename) {
        try {
            File file = fileRepository.findFileByFileName(filename);
            if (file == null) {
                throw new NotFoundException("File with filename " + filename + " not found.");
            }
            return FileMapper.toFileDTO(file);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while fetching file with name " + filename + " from the database.");
        }
    }

    // ------------------------------| Creating |------------------------------
    // Add a new file
    @Transactional
    public FileDTO saveFile(FileDTO fileDTO) {
        try{
            File file = FileMapper.toFileEntity(fileDTO);
            file.setUploadDate(ZonedDateTime.now());
            fileRepository.saveFile(file);
            return FileMapper.toFileDTO(file);
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while adding a new file to the database.");
        }   
    }

    // ------------------------------| Deleting |------------------------------
    // Delete a file by id.
    @Transactional
    public void deleteFile(Long fileId) {
        try {
            File file = fileRepository.findFileById(fileId);
            if (file == null) {
                throw new NotFoundException("File with ID " + fileId + " not found.");
            }
            fileRepository.deleteFileById(fileId);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while deleting file with ID " + fileId + " from the database.");
        }
    }

    // Delete all files
    public void deleteAllFiles() {
        try {
            fileRepository.deleteAllFiles();
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while deleting all files from the database.");
        }
    }

    // ------------------------------| Utility Methods |------------------------------
    // Count all files
    public long countFiles() {
        try {
            return fileRepository.countFiles();
        } catch (Exception e) {
            throw new DatabaseException("An database error occured while counting files in the database.");
        }
    }
}
