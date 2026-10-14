package com.muawiyah.repository.service;

import com.muawiyah.repository.dto.PaperSubmissionForm;
import com.muawiyah.repository.model.Paper;
import com.muawiyah.repository.model.ReviewStatus;
import com.muawiyah.repository.repository.PaperRepository;
import com.muawiyah.repository.service.impl.PaperServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for PaperServiceImpl.
 *
 * @ExtendWith(MockitoExtension.class) — loads Mockito without starting Spring.
 * @Mock PaperRepository — provides a fake repository that does NOT touch H2.
 * @InjectMocks PaperServiceImpl — creates the real service and injects the mock.
 *
 * These tests verify business logic in complete isolation.
 */
@ExtendWith(MockitoExtension.class)
class PaperServiceTest {

    @Mock
    private PaperRepository paperRepository;

    @InjectMocks
    private PaperServiceImpl paperService;

    private PaperSubmissionForm validForm() {
        PaperSubmissionForm form = new PaperSubmissionForm();
        form.setTitle("Distributed Consensus in Modern Systems");
        form.setAuthors("Leslie Lamport");
        form.setAbstractText("A comprehensive analysis of the Paxos consensus algorithm and its applications.");
        form.setKeywords("Consensus, Paxos, Distributed Systems");
        form.setPublicationYear(2024);
        form.setDoiUrl("https://doi.org/10.1000/test");
        form.setSubmitterEmail("leslie@systems.org");
        return form;
    }

    @Test
    @DisplayName("Should save Paper with all fields correctly mapped from the form DTO")
    void shouldMapFormFieldsToPaperEntity() {
        Paper mockSaved = new Paper();
        mockSaved.setId(1L);
        when(paperRepository.save(any(Paper.class))).thenReturn(mockSaved);

        paperService.submitPaper(validForm(), "lamport");

        // Capture the Paper that was passed to save()
        ArgumentCaptor<Paper> captor = ArgumentCaptor.forClass(Paper.class);
        verify(paperRepository).save(captor.capture());

        Paper captured = captor.getValue();
        assertThat(captured.getTitle()).isEqualTo("Distributed Consensus in Modern Systems");
        assertThat(captured.getAuthors()).isEqualTo("Leslie Lamport");
        assertThat(captured.getKeywords()).isEqualTo("Consensus, Paxos, Distributed Systems");
        assertThat(captured.getPublicationYear()).isEqualTo(2024);
        assertThat(captured.getDoiUrl()).isEqualTo("https://doi.org/10.1000/test");
        assertThat(captured.getSubmitterEmail()).isEqualTo("leslie@systems.org");
    }

    @Test
    @DisplayName("Should always assign SUBMITTED status regardless of any other state")
    void shouldAssignSubmittedStatusOnCreation() {
        when(paperRepository.save(any(Paper.class))).thenAnswer(inv -> inv.getArgument(0));

        Paper result = paperService.submitPaper(validForm(), "lamport");

        assertThat(result.getReviewStatus()).isEqualTo(ReviewStatus.SUBMITTED);
    }

    @Test
    @DisplayName("Should assign submitterUsername from server-side principal, not form data")
    void shouldAssignSubmitterUsernameFromPrincipal() {
        when(paperRepository.save(any(Paper.class))).thenAnswer(inv -> inv.getArgument(0));

        Paper result = paperService.submitPaper(validForm(), "principal_user");

        assertThat(result.getSubmitterUsername()).isEqualTo("principal_user");
    }

    @Test
    @DisplayName("Should return the persisted Paper entity returned by the repository")
    void shouldReturnSavedPaperFromRepository() {
        Paper savedPaper = new Paper();
        savedPaper.setId(42L);
        when(paperRepository.save(any(Paper.class))).thenReturn(savedPaper);

        Paper result = paperService.submitPaper(validForm(), "researcher");

        assertThat(result.getId()).isEqualTo(42L);
    }
}
