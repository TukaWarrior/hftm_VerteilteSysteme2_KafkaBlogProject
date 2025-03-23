package ch.hftm.blogproject.util;

import ch.hftm.blogproject.model.dto.FileDTO;
import ch.hftm.blogproject.model.entity.FileEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileMapper {

    public FileDTO toFileDTO(FileEntity fileEntity) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(fileEntity.getId());
        fileDTO.setFileName(fileEntity.getFileName());
        fileDTO.setContentType(fileEntity.getContentType());
        fileDTO.setFileSize(fileEntity.getFileSize());
        fileDTO.setUploadDate(fileEntity.getUploadDate());
        fileDTO.setChecksum(fileEntity.getChecksum());
        return fileDTO;
    }

    public FileEntity toFileEntity(FileDTO fileDTO) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(fileDTO.getFileName());
        fileEntity.setContentType(fileDTO.getContentType());
        fileEntity.setFileSize(fileDTO.getFileSize());
        fileEntity.setUploadDate(fileDTO.getUploadDate());
        fileEntity.setChecksum(fileDTO.getChecksum());
        return fileEntity;
    }
}