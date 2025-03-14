package ch.hftm.blogproject.model.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class File {

    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private ZonedDateTime uploadDate;
    private String storageKey;
    private String checksum;

    // Constructors
    public File() {}

    public File(String fileName, String fileType, Long fileSize, ZonedDateTime uploadDate, String storageKey, String checksum) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.uploadDate = uploadDate;
        this.storageKey = storageKey;
        this.checksum = checksum;
    }

    // Getters and Setters
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileType() {
        return this.fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public Long getFileSize() {
        return this.fileSize;
    }
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    public ZonedDateTime getUploadDate() {
        return this.uploadDate;
    }
    public void setUploadDate(ZonedDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
    public String getStorageKey() {
        return this.storageKey;
    }
    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }
    public String getChecksum() {
        return this.checksum;
    }
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
