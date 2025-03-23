package ch.hftm.blogproject.util;

import ch.hftm.blogproject.model.dto.FileMetadataDTO;
import ch.hftm.blogproject.model.entity.FileMetadataEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileMetadataMapper {

    public FileMetadataDTO toFileMetadataDTO(FileMetadataEntity fileMetadataEntity) {
        FileMetadataDTO fileMetadataDTO = new FileMetadataDTO();
        fileMetadataDTO.setId(fileMetadataEntity.getId());
        fileMetadataDTO.setFileName(fileMetadataEntity.getFileName());
        fileMetadataDTO.setContentType(fileMetadataEntity.getContentType());
        fileMetadataDTO.setFileSize(fileMetadataEntity.getFileSize());
        fileMetadataDTO.setUploadDate(fileMetadataEntity.getUploadDate());
        fileMetadataDTO.setChecksum(fileMetadataEntity.getChecksum());
        return fileMetadataDTO;
    }

    public FileMetadataEntity toFileMetadataEntity(FileMetadataDTO fileMetadataDTO) {
        FileMetadataEntity fileMetadataEntity = new FileMetadataEntity();
        fileMetadataEntity.setFileName(fileMetadataDTO.getFileName());
        fileMetadataEntity.setContentType(fileMetadataDTO.getContentType());
        fileMetadataEntity.setFileSize(fileMetadataDTO.getFileSize());
        fileMetadataEntity.setUploadDate(fileMetadataDTO.getUploadDate());
        fileMetadataEntity.setChecksum(fileMetadataDTO.getChecksum());
        return fileMetadataEntity;
    }
}