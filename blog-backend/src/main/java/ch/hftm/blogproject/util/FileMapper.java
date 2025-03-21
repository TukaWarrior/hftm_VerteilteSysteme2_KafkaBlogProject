package ch.hftm.blogproject.util;

import ch.hftm.blogproject.model.dto.FileDTO;
import ch.hftm.blogproject.model.entity.File;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileMapper {

    public static FileDTO toFileDTO(File fileEntity) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(fileEntity.getId());
        fileDTO.setFileName(fileEntity.getFileName());
        fileDTO.setContentType(fileEntity.getContentType());
        fileDTO.setFileSize(fileEntity.getFileSize());
        fileDTO.setUploadDate(fileEntity.getUploadDate());
        fileDTO.setChecksum(fileEntity.getChecksum());
        return fileDTO;
    }

    public static FileDTO toFileDTOWithFileData(File fileEntity, byte[] fileData) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(fileEntity.getId());
        fileDTO.setFileName(fileEntity.getFileName());
        fileDTO.setContentType(fileEntity.getContentType());
        fileDTO.setFileSize(fileEntity.getFileSize());
        fileDTO.setUploadDate(fileEntity.getUploadDate());
        fileDTO.setChecksum(fileEntity.getChecksum());
        fileDTO.setFileData(fileData);
        return fileDTO;
    }

    public static File toFileEntity(FileDTO fileDTO) {
        File fileEntity = new File();
        fileEntity.setFileName(fileDTO.getFileName());
        fileEntity.setContentType(fileDTO.getContentType());
        fileEntity.setFileSize(fileDTO.getFileSize());
        fileEntity.setUploadDate(fileDTO.getUploadDate());
        fileEntity.setChecksum(fileDTO.getChecksum());
        return fileEntity;
    }
}