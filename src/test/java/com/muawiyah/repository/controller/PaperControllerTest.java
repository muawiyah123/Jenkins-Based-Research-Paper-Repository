package com.muawiyah.repository.controller;

import com.muawiyah.repository.model.Paper;
import com.muawiyah.repository.model.ReviewStatus;
import com.muawiyah.repository.service.PaperService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Controller slice tests for PaperController.
 *
 * @WebMvcTest(PaperController.class) — boots only the web layer (no JPA, no database).
 * @MockBean PaperService — replaces the real service with a mock so no data is saved.
 *
 * This verifies:
 * SUBMISSION WORKFLOW
 * 1. GET /papers/submit renders the form correctly.
 * 2. A valid POST redirects to the paper detail page with a flash success message.
 * 3. An invalid POST re-renders the form with field-level errors.
 * 4. An invalid email re-renders the form with an email error.
 *
 * CATALOG
 * 5. GET /papers returns the catalog view with a papers model attribute.
 * 6. GET /papers?q=keyword returns filtered search results.
 *
 * DETAIL
 * 7. GET /papers/{id} for an existing paper returns the detail view.
 * 8. GET /papers/{id} for a non-existent paper redirects to /papers with an error flash.
 */
@WebMvcTest(PaperController.class)
class PaperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaperService paperService;

    // ─── Helper factory ───────────────────────────────────────────────────────

    private Paper buildPaper(Long id, String title) {
        Paper p = new Paper();
        p.setId(id);
        p.setTitle(title);
        p.setAuthors("Test Author");
        p.setAbstractText("A test abstract of sufficient length for display.");
        p.setKeywords("Java, Spring, Testing");
        p.setPublicationYear(2024);
        p.setSubmitterUsername("researcher");
        p.setSubmitterEmail("test@test.com");
        p.setReviewStatus(ReviewStatus.SUBMITTED);
        return p;
    }

    // ─── Submission tests ─────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /papers/submit should return HTTP 200 with empty form and correct view")
    void shouldRenderSubmitFormOnGetRequest() throws Exception {
        mockMvc.perform(get("/papers/submit"))
               .andExpect(status().isOk())
               .andExpect(view().name("papers/submit"))
               .andExpect(model().attributeExists("paperForm"))
               .andExpect(model().attributeExists("pageTitle"));
    }

    @Test
    @DisplayName("POST /papers/submit with valid data should redirect to detail page with success flash")
    void shouldRedirectToDetailPageOnValidSubmission() throws Exception {
        Paper mockPaper = buildPaper(5L, "Valid Submission Title Here");
        when(paperService.submitPaper(any(), anyString())).thenReturn(mockPaper);

        mockMvc.perform(post("/papers/submit")
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .param("title", "Valid Submission Title Here")
               .param("authors", "Test Author Name")
               .param("abstractText", "This is a sufficiently long abstract for the test to pass validation rules.")
               .param("keywords", "Testing, Spring, Java")
               .param("publicationYear", "2024")
               .param("submitterEmail", "author@test.com"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/papers/5"))
               .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @DisplayName("POST /papers/submit with blank required fields should re-render form with errors")
    void shouldReRenderFormWhenRequiredFieldsAreMissing() throws Exception {
        mockMvc.perform(post("/papers/submit")
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .param("title", "")
               .param("authors", "")
               .param("abstractText", "")
               .param("keywords", "")
               .param("submitterEmail", ""))
               .andExpect(status().isOk())
               .andExpect(view().name("papers/submit"))
               .andExpect(model().attributeHasFieldErrors("paperForm",
                       "title", "authors", "abstractText", "keywords", "submitterEmail"));
    }

    @Test
    @DisplayName("POST /papers/submit with invalid email should re-render form with email error")
    void shouldRejectInvalidEmailFormat() throws Exception {
        mockMvc.perform(post("/papers/submit")
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .param("title", "Valid Title That Is Long Enough")
               .param("authors", "Valid Author")
               .param("abstractText", "This abstract is certainly long enough to pass the minimum length validation.")
               .param("keywords", "Java, Testing")
               .param("publicationYear", "2024")
               .param("submitterEmail", "not-a-valid-email"))
               .andExpect(status().isOk())
               .andExpect(view().name("papers/submit"))
               .andExpect(model().attributeHasFieldErrors("paperForm", "submitterEmail"));
    }

    // ─── Catalog tests ────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /papers should return catalog view with papers model attribute")
    void shouldRenderCatalogWithAllPapers() throws Exception {
        List<Paper> paperList = List.of(
                buildPaper(1L, "First Paper"),
                buildPaper(2L, "Second Paper")
        );
        Page<Paper> paperPage = new PageImpl<>(paperList, PageRequest.of(0, 10), 2);
        when(paperService.findAll(any())).thenReturn(paperPage);

        mockMvc.perform(get("/papers"))
               .andExpect(status().isOk())
               .andExpect(view().name("papers/catalog"))
               .andExpect(model().attributeExists("papers"))
               .andExpect(model().attributeExists("query"))
               .andExpect(model().attributeExists("pageTitle"));
    }

    @Test
    @DisplayName("GET /papers?q=keyword should invoke search and return filtered results")
    void shouldDelegateToSearchServiceWhenQueryIsPresent() throws Exception {
        List<Paper> results = List.of(buildPaper(3L, "Search Result Paper"));
        Page<Paper> resultPage = new PageImpl<>(results, PageRequest.of(0, 10), 1);
        when(paperService.search(eq("spring"), any())).thenReturn(resultPage);

        mockMvc.perform(get("/papers").param("q", "spring"))
               .andExpect(status().isOk())
               .andExpect(view().name("papers/catalog"))
               .andExpect(model().attribute("query", "spring"));
    }

    // ─── Detail tests ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /papers/{id} for existing paper should return detail view with paper attribute")
    void shouldRenderDetailPageForExistingPaper() throws Exception {
        Paper paper = buildPaper(7L, "Existing Paper Title");
        when(paperService.findById(7L)).thenReturn(Optional.of(paper));

        mockMvc.perform(get("/papers/7"))
               .andExpect(status().isOk())
               .andExpect(view().name("papers/detail"))
               .andExpect(model().attributeExists("paper"))
               .andExpect(model().attributeExists("pageTitle"));
    }

    @Test
    @DisplayName("GET /papers/{id} for non-existent paper should redirect to catalog with error flash")
    void shouldRedirectToCatalogWhenPaperNotFound() throws Exception {
        when(paperService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/papers/999"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/papers"))
               .andExpect(flash().attributeExists("errorMessage"));
    }

    // ─── Privacy tests ────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /papers catalog view must NEVER expose submitterEmail")
    void shouldNotExposeSubmitterEmailInCatalogHtml() throws Exception {
        Paper paper = buildPaper(1L, "Privacy Protected Paper");
        paper.setSubmitterEmail("secret-author@university.edu");
        Page<Paper> page = new PageImpl<>(List.of(paper), PageRequest.of(0, 10), 1);
        when(paperService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/papers"))
               .andExpect(status().isOk())
               .andExpect(content().string(not(containsString("secret-author@university.edu"))));
    }

    @Test
    @DisplayName("GET /papers/{id} detail view must NEVER expose submitterEmail")
    void shouldNotExposeSubmitterEmailInDetailHtml() throws Exception {
        Paper paper = buildPaper(1L, "Privacy Protected Paper");
        paper.setSubmitterEmail("secret-author@university.edu");
        when(paperService.findById(1L)).thenReturn(Optional.of(paper));

        mockMvc.perform(get("/papers/1"))
               .andExpect(status().isOk())
               .andExpect(content().string(not(containsString("secret-author@university.edu"))));
    }
}
