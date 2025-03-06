package ch.hftm.blogproject.model.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blogID")
    private Long blogID;
    private String title;
    private String content;
    private String creator;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastChangedAt;
    private boolean validationStatus;

    // Default constructor
    public Blog() {
    }

    // All-args constructor
    public Blog(Long blogID, String title, String content, String creator, ZonedDateTime createdAt, ZonedDateTime lastChangedAt, boolean validationStatus) {
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
        return blogID;
    }

    public void setBlogID(Long blogID) {
        this.blogID = blogID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
