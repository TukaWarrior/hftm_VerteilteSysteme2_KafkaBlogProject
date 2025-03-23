package ch.hftm.blogproject.BasicExample;

public class ExampleFileDTO {
    
    private Long id;
    private String fileName;
    private String contentType;

    // Constructors
    public ExampleFileDTO(){}

    public ExampleFileDTO(String fileName, String contentType){
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
