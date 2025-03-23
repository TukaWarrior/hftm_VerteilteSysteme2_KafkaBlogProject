package ch.hftm.blogproject.model.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FileMetadataEntity {

    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "id")
    private Long id;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private ZonedDateTime uploadDate;
    private String checksum;

    // Constructors
    public FileMetadataEntity() {}

    public FileMetadataEntity(String fileName, String contentType, Long fileSize, ZonedDateTime uploadDate, String checksum) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.uploadDate = uploadDate;
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
}
