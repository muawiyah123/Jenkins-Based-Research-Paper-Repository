package com.muawiyah.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * JPA Entity representing a Research Paper record in the repository.
 * Contains exactly 12 fields defined in the database design specification.
 */
@Entity
@Table(
    name = "papers",
    indexes = {
        @Index(name = "idx_papers_status", columnList = "review_status"),
        @Index(name = "idx_papers_year", columnList = "publication_year"),
        @Index(name = "idx_papers_submitter", columnList = "submitter_username")
    }
)
public class Paper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    @Column(nullable = false, length = 255)
    private String title;

    @NotBlank(message = "Authors are required")
    @Size(min = 2, max = 255, message = "Authors must be between 2 and 255 characters")
    @Column(nullable = false, length = 255)
    private String authors;

    @NotBlank(message = "Abstract is required")
    @Size(min = 20, max = 4000, message = "Abstract must be between 20 and 4000 characters")
    @Column(name = "abstract_text", nullable = false, length = 4000)
    private String abstractText;

    @NotBlank(message = "Keywords are required")
    @Size(min = 2, max = 255, message = "Keywords must be between 2 and 255 characters")
    @Column(nullable = false, length = 255)
    private String keywords;

    @NotNull(message = "Publication year is required")
    @Min(value = 1900, message = "Publication year must be 1900 or later")
    @Max(value = 2030, message = "Publication year cannot exceed the upcoming calendar year")
    @Column(name = "publication_year", nullable = false)
    private Integer publicationYear;

    @Size(max = 255, message = "DOI or URL must not exceed 255 characters")
    @Pattern(regexp = "^(https?://.+)?$", message = "DOI or URL must be a valid HTTP or HTTPS URL if provided")
    @Column(name = "doi_url", length = 255)
    private String doiUrl;

    /**
     * Submitter email is strictly private and never rendered on public views.
     */
    @NotBlank(message = "Submitter email is required")
    @Email(message = "Submitter email must be a valid email address")
    @Size(max = 255, message = "Submitter email must not exceed 255 characters")
    @Column(name = "submitter_email", nullable = false, length = 255)
    private String submitterEmail;

    /**
     * Immutable ownership username derived server-side from Authentication.getName().
     */
    @NotBlank(message = "Submitter username is required")
    @Size(max = 100, message = "Submitter username must not exceed 100 characters")
    @Column(name = "submitter_username", nullable = false, updatable = false, length = 100)
    private String submitterUsername;

    @NotNull(message = "Review status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "review_status", nullable = false, length = 30)
    private ReviewStatus reviewStatus = ReviewStatus.SUBMITTED;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Paper() {
    }

    public Paper(String title, String authors, String abstractText, String keywords,
                 Integer publicationYear, String doiUrl, String submitterEmail,
                 String submitterUsername) {
        this.title = title;
        this.authors = authors;
        this.abstractText = abstractText;
        this.keywords = keywords;
        this.publicationYear = publicationYear;
        this.doiUrl = doiUrl;
        this.submitterEmail = submitterEmail;
        this.submitterUsername = submitterUsername;
        this.reviewStatus = ReviewStatus.SUBMITTED;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.reviewStatus == null) {
            this.reviewStatus = ReviewStatus.SUBMITTED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getDoiUrl() {
        return doiUrl;
    }

    public void setDoiUrl(String doiUrl) {
        this.doiUrl = doiUrl;
    }

    public String getSubmitterEmail() {
        return submitterEmail;
    }

    public void setSubmitterEmail(String submitterEmail) {
        this.submitterEmail = submitterEmail;
    }

    public String getSubmitterUsername() {
        return submitterUsername;
    }

    public void setSubmitterUsername(String submitterUsername) {
        this.submitterUsername = submitterUsername;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paper paper = (Paper) o;
        return Objects.equals(id, paper.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Paper{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", publicationYear=" + publicationYear +
                ", reviewStatus=" + reviewStatus +
                '}';
    }
}
