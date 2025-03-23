package ch.hftm.blogproject.model.exception;

public class ExceptionDTO {

    // Fields
    private String exception;
    private String message;

    // Constructors
    public ExceptionDTO() {}
    public ExceptionDTO(String exception, String message) {
        this.exception = exception;
        this.message = message;
    }

    // Getters and Setters
    public String getException() {
        return this.exception;
    }
    public void setException(String exception) {
        this.exception = exception;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
