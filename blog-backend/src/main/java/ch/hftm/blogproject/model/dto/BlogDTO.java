package ch.hftm.blogproject.model.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;

// @Data
// @AllArgsConstructor
// @NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class BlogDTO {

    private Long blogID;
    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotBlank(message = "Content cannot be empty")
    private String content;
    @NotBlank(message = "Creator cannot be empty")
    private String creator;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastChangedAt;
    private boolean validationStatus;

    // Default constructor
    public BlogDTO() {
    }

    // All-args constructor
    public BlogDTO(Long blogID, String title, String content, String creator, ZonedDateTime createdAt, ZonedDateTime lastChangedAt, boolean validationStatus) {
        this.blogID = blogID;
        this.title = title;
        this.content = content;
        this.creator = creator;
        this.createdAt = createdAt;
        this.lastChangedAt = lastChangedAt;
        this.validationStatus = validationStatus;
    }

    // Getters and Setters
    public Long getBlogID() {
        return this.blogID;
    }

    public void setBlogID(Long blogID) {
        this.blogID = blogID;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastChangedAt() {
        return this.lastChangedAt;
    }

    public void setLastChangedAt(ZonedDateTime lastChangedAt) {
        this.lastChangedAt = lastChangedAt;
    }

    public boolean getValidationStatus() {
        return this.validationStatus;
    }

    public void setValidationStatus(boolean validationStatus) {
        this.validationStatus = validationStatus;
    }
}
