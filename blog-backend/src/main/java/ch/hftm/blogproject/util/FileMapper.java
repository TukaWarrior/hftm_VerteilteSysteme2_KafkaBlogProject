package ch.hftm.blogproject.util;

import ch.hftm.blogproject.model.dto.FileDTO;
import ch.hftm.blogproject.model.entity.File;

public class FileMapper {

    public static FileDTO toFileDTO(File fileEntity) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(fileEntity.getId());
        fileDTO.setFileName(fileEntity.getFileName());
        fileDTO.setFileType(fileEntity.getFileType());
        fileDTO.setFileSize(fileEntity.getFileSize());
        fileDTO.setUploadDate(fileEntity.getUploadDate());
        fileDTO.setStorageKey(fileEntity.getStorageKey());
        fileDTO.setChecksum(fileEntity.getChecksum());
        return fileDTO;
    }

    public static FileDTO toFileDTOWithFileData(File fileEntity, byte[] fileData) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(fileEntity.getId());
        fileDTO.setFileName(fileEntity.getFileName());
        fileDTO.setFileType(fileEntity.getFileType());
        fileDTO.setFileSize(fileEntity.getFileSize());
        fileDTO.setUploadDate(fileEntity.getUploadDate());
        fileDTO.setStorageKey(fileEntity.getStorageKey());
        fileDTO.setChecksum(fileEntity.getChecksum());
        fileDTO.setFileData(fileData);
        return fileDTO;
    }
    
    public static File toFileEntity(FileDTO fileDTO) {
        File fileEntity = new File();
        fileEntity.setFileName(fileDTO.getFileName());
        fileEntity.setFileType(fileDTO.getFileType());
        fileEntity.setFileSize(fileDTO.getFileSize());
        fileEntity.setUploadDate(fileDTO.getUploadDate());
        fileEntity.setStorageKey(fileDTO.getStorageKey());
        fileEntity.setChecksum(fileDTO.getChecksum());
        return fileEntity;
    }
}
