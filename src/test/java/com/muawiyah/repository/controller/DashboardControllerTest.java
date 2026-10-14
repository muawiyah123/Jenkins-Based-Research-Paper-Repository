package com.muawiyah.repository.controller;

import com.muawiyah.repository.config.SecurityConfig;
import com.muawiyah.repository.model.Paper;
import com.muawiyah.repository.model.ReviewStatus;
import com.muawiyah.repository.service.PaperService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Controller tests for DashboardController (Task 11, US-008).
 *
 * Verifies Acceptance Criteria:
 * - AC-008-1: Authenticated user navigates to GET /dashboard -> HTTP 200 with 6 metric cards.
 * - AC-008-2: Exact metric values (Total: 8, Submitted: 1, Under Review: 2, Revision Requested: 1, Accepted: 3, Rejected: 1).
 * - AC-008-3: Unauthenticated visitor accessing GET /dashboard is redirected to /login with HTTP 302.
 */
@WebMvcTest(DashboardController.class)
@Import(SecurityConfig.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaperService paperService;

    private Map<ReviewStatus, Long> createSampleStatusCounts() {
        Map<ReviewStatus, Long> map = new EnumMap<>(ReviewStatus.class);
        map.put(ReviewStatus.SUBMITTED, 1L);
        map.put(ReviewStatus.UNDER_REVIEW, 2L);
        map.put(ReviewStatus.REVISION_REQUESTED, 1L);
        map.put(ReviewStatus.ACCEPTED, 3L);
        map.put(ReviewStatus.REJECTED, 1L);
        return map;
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("GET /dashboard authenticated as ADMIN should render dashboard with all 6 metric cards (AC-008-1, AC-008-2)")
    void shouldRenderDashboardWithExactCountsForAdmin() throws Exception {
        when(paperService.countTotalPapers()).thenReturn(8L);
        when(paperService.getReviewStatusCounts()).thenReturn(createSampleStatusCounts());

        Paper sample = new Paper();
        sample.setId(1L);
        sample.setTitle("Sample Paper for Dashboard");
        sample.setAuthors("Jane Doe");
        sample.setPublicationYear(2025);
        sample.setReviewStatus(ReviewStatus.SUBMITTED);
        sample.setCreatedAt(LocalDateTime.now());
        when(paperService.getRecentSubmissions(5)).thenReturn(List.of(sample));

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("totalPapers", 8L))
                .andExpect(model().attribute("submittedCount", 1L))
                .andExpect(model().attribute("underReviewCount", 2L))
                .andExpect(model().attribute("revisionRequestedCount", 1L))
                .andExpect(model().attribute("acceptedCount", 3L))
                .andExpect(model().attribute("rejectedCount", 1L))
                .andExpect(content().string(containsString("id=\"metric-total-papers\"")))
                .andExpect(content().string(containsString("id=\"metric-submitted\"")))
                .andExpect(content().string(containsString("id=\"metric-under-review\"")))
                .andExpect(content().string(containsString("id=\"metric-revision-requested\"")))
                .andExpect(content().string(containsString("id=\"metric-accepted\"")))
                .andExpect(content().string(containsString("id=\"metric-rejected\"")))
                .andExpect(content().string(containsString("Sample Paper for Dashboard")));
    }

    @Test
    @WithMockUser(username = "researcher", roles = "RESEARCHER")
    @DisplayName("GET /dashboard authenticated as RESEARCHER should be permitted (AC-008-1)")
    void shouldAllowResearcherToAccessDashboard() throws Exception {
        when(paperService.countTotalPapers()).thenReturn(0L);
        when(paperService.getReviewStatusCounts()).thenReturn(Collections.emptyMap());
        when(paperService.getRecentSubmissions(5)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    @WithMockUser(username = "reviewer", roles = "REVIEWER")
    @DisplayName("GET /dashboard authenticated as REVIEWER should be permitted (AC-008-1)")
    void shouldAllowReviewerToAccessDashboard() throws Exception {
        when(paperService.countTotalPapers()).thenReturn(0L);
        when(paperService.getReviewStatusCounts()).thenReturn(Collections.emptyMap());
        when(paperService.getRecentSubmissions(5)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    @DisplayName("GET /dashboard unauthenticated should redirect to /login with HTTP 302 (AC-008-3)")
    void shouldRedirectUnauthenticatedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
