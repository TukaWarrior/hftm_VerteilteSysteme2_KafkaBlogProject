package ch.hftm.blogproject.model.dto;

public class ImageDTO {
    
    private Long id;
    private String fileName;
    private String contentType;

    // Constructors
    public ImageDTO(){}

    public ImageDTO(String fileName, String contentType){
        this.fileName = fileName;
        this.contentType = contentType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
