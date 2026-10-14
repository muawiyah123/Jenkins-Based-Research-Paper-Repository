package com.muawiyah.repository.dto;

import com.muawiyah.repository.model.Paper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for the paper submission HTML form.
 *
 * WHY A SEPARATE DTO?
 * The Paper JPA entity maps directly to the database table and contains
 * server-assigned fields (submitterUsername, reviewStatus, createdAt, updatedAt)
 * that must never be accepted from an HTTP request. This DTO holds only the
 * fields a researcher is permitted to supply. The service layer converts it
 * into a Paper entity before saving.
 */
public class PaperSubmissionForm {

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title;

    @NotBlank(message = "Authors are required")
    @Size(min = 2, max = 255, message = "Authors must be between 2 and 255 characters")
    private String authors;

    @NotBlank(message = "Abstract is required")
    @Size(min = 20, max = 4000, message = "Abstract must be between 20 and 4000 characters")
    private String abstractText;

    @NotBlank(message = "Keywords are required")
    @Size(min = 2, max = 255, message = "Keywords must be between 2 and 255 characters")
    private String keywords;

    @NotNull(message = "Publication year is required")
    @Min(value = 1900, message = "Publication year must be 1900 or later")
    @Max(value = 2030, message = "Publication year cannot be in the far future")
    private Integer publicationYear;

    @Size(max = 255, message = "DOI or URL must not exceed 255 characters")
    @Pattern(regexp = "^(https?://.+)?$",
             message = "DOI or URL must be a valid HTTP or HTTPS URL if provided")
    private String doiUrl;

    @NotBlank(message = "Submitter email is required")
    @Email(message = "Submitter email must be a valid email address")
    @Size(max = 255)
    private String submitterEmail;

    public PaperSubmissionForm() {
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }

    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }

    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public String getDoiUrl() { return doiUrl; }
    public void setDoiUrl(String doiUrl) { this.doiUrl = doiUrl; }

    public String getSubmitterEmail() { return submitterEmail; }
    public void setSubmitterEmail(String submitterEmail) { this.submitterEmail = submitterEmail; }

    /**
     * Factory method to pre-populate form from an existing Paper entity.
     */
    public static PaperSubmissionForm fromPaper(Paper paper) {
        PaperSubmissionForm form = new PaperSubmissionForm();
        form.setTitle(paper.getTitle());
        form.setAuthors(paper.getAuthors());
        form.setAbstractText(paper.getAbstractText());
        form.setKeywords(paper.getKeywords());
        form.setPublicationYear(paper.getPublicationYear());
        form.setDoiUrl(paper.getDoiUrl());
        form.setSubmitterEmail(paper.getSubmitterEmail());
        return form;
    }
}
