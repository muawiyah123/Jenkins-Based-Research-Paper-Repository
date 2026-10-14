package com.muawiyah.repository.controller;

import com.muawiyah.repository.model.Paper;
import com.muawiyah.repository.service.PaperService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Controller slice tests for PaperController.
 *
 * @WebMvcTest(PaperController.class) — boots only the web layer (no JPA, no database).
 * @MockBean PaperService — replaces the real service with a mock so no data is saved.
 *
 * This verifies:
 * 1. The GET endpoint renders the form correctly.
 * 2. A valid POST redirects to home with a flash success message.
 * 3. An invalid POST re-renders the form with field-level errors.
 */
@WebMvcTest(PaperController.class)
class PaperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaperService paperService;

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
    @DisplayName("POST /papers/submit with valid data should redirect to / with success flash message")
    void shouldRedirectToHomeOnValidSubmission() throws Exception {
        Paper mockPaper = new Paper();
        mockPaper.setId(5L);
        mockPaper.setTitle("Valid Submission Title Here");
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
               .andExpect(redirectedUrl("/"))
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
}
