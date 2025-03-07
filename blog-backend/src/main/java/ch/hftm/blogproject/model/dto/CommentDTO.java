package ch.hftm.blogproject.model.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class CommentDTO {

    private Long commentID;
    private Long blogID;
    @NotBlank(message = "Content cannot be empty")
    private String content;
    private String creator;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastChangedAt;
    private boolean validationStatus;

    // Default constructor
    public CommentDTO() {
    }

    // All-args constructor
    public CommentDTO(Long commentID, Long blogID, String content, String creator, ZonedDateTime createdAt, ZonedDateTime lastChangedAt, boolean validationStatus) {
        this.commentID = commentID;
        this.blogID = blogID;
        this.content = content;
        this.creator = creator;
        this.createdAt = createdAt;
        this.lastChangedAt = lastChangedAt;
        this.validationStatus = validationStatus;
    }

    // Getters and Setters
    public Long getCommentID() {
        return commentID;
    }

    public void setCommentID(Long commentID) {
        this.commentID = commentID;
    }

    public Long getBlogID() {
        return blogID;
    }

    public void setBlogID(Long blogID) {
        this.blogID = blogID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastChangedAt() {
        return lastChangedAt;
    }

    public void setLastChangedAt(ZonedDateTime lastChangedAt) {
        this.lastChangedAt = lastChangedAt;
    }

    public boolean getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(boolean validationStatus) {
        this.validationStatus = validationStatus;
    }
}