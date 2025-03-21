package ch.hftm.blogproject.model.dto;

import java.time.ZonedDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// Same as the File object class but with a byte array for the file data added.
public class FileDTO {

    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private ZonedDateTime uploadDate;
    private String checksum;
    private byte[] fileData;

    // Constructors
    public FileDTO() {}

    public FileDTO(String fileName, String contentType, Long fileSize, ZonedDateTime uploadDate, String storageKey, String checksum, byte[] fileData) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.uploadDate = uploadDate;
        this.checksum = checksum;
        this.fileData = fileData;
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
    public String getContentType() {
        return this.contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
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
    public String getChecksum() {
        return this.checksum;
    }
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
    public byte[] getFileData() {
        return this.fileData;
    }
    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
