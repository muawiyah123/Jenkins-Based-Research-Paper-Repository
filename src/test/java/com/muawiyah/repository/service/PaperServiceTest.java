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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    // ─── submitPaper tests ────────────────────────────────────────────────────

    @Test
    @DisplayName("Should save Paper with all fields correctly mapped from the form DTO")
    void shouldMapFormFieldsToPaperEntity() {
        Paper mockSaved = new Paper();
        mockSaved.setId(1L);
        when(paperRepository.save(any(Paper.class))).thenReturn(mockSaved);

        paperService.submitPaper(validForm(), "lamport");

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

    // ─── findAll tests ────────────────────────────────────────────────────────

    @Test
    @DisplayName("findAll should delegate to repository and return the same Page")
    void shouldDelegateFindAllToRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Paper p1 = new Paper(); p1.setId(1L);
        Paper p2 = new Paper(); p2.setId(2L);
        Page<Paper> expected = new PageImpl<>(List.of(p1, p2), pageable, 2);
        when(paperRepository.findAll(pageable)).thenReturn(expected);

        Page<Paper> result = paperService.findAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).containsExactly(p1, p2);
        verify(paperRepository).findAll(pageable);
    }

    @Test
    @DisplayName("findAll should return an empty Page when no papers exist")
    void shouldReturnEmptyPageWhenNoPapersExist() {
        Pageable pageable = PageRequest.of(0, 10);
        when(paperRepository.findAll(pageable)).thenReturn(Page.empty(pageable));

        Page<Paper> result = paperService.findAll(pageable);

        assertThat(result.isEmpty()).isTrue();
    }

    // ─── findById tests ───────────────────────────────────────────────────────

    @Test
    @DisplayName("findById should return the paper wrapped in Optional when it exists")
    void shouldReturnPaperWhenIdExists() {
        Paper paper = new Paper();
        paper.setId(10L);
        when(paperRepository.findById(10L)).thenReturn(Optional.of(paper));

        Optional<Paper> result = paperService.findById(10L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("findById should return empty Optional for a non-existent ID")
    void shouldReturnEmptyOptionalForNonExistentId() {
        when(paperRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Paper> result = paperService.findById(999L);

        assertThat(result).isEmpty();
    }

    // ─── search tests ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("search should delegate to searchByKeyword repository query")
    void shouldDelegateSearchToRepositoryQuery() {
        Pageable pageable = PageRequest.of(0, 10);
        Paper match = new Paper(); match.setId(3L); match.setTitle("Machine Learning Paper");
        Page<Paper> expected = new PageImpl<>(List.of(match), pageable, 1);
        when(paperRepository.searchByKeyword("machine", pageable)).thenReturn(expected);

        Page<Paper> result = paperService.search("machine", pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Machine Learning Paper");
        verify(paperRepository).searchByKeyword("machine", pageable);
    }

    // ─── updatePaper tests (Task 8) ───────────────────────────────────────────

    @Test
    @DisplayName("updatePaper should update metadata for paper in SUBMITTED status by author")
    void shouldUpdatePaperWhenAuthorAndStatusAreValid() {
        Paper existing = new Paper();
        existing.setId(1L);
        existing.setTitle("Old Title");
        existing.setSubmitterUsername("researcher");
        existing.setReviewStatus(ReviewStatus.SUBMITTED);

        when(paperRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(paperRepository.save(any(Paper.class))).thenAnswer(inv -> inv.getArgument(0));

        PaperSubmissionForm form = new PaperSubmissionForm();
        form.setTitle("Updated Title");
        form.setAuthors("Updated Authors");
        form.setAbstractText("Updated abstract of sufficient length.");
        form.setKeywords("Java, Testing");
        form.setPublicationYear(2025);
        form.setDoiUrl("https://doi.org/10.1234/updated");
        form.setSubmitterEmail("new@email.com");

        Paper updated = paperService.updatePaper(1L, form, "researcher");

        assertThat(updated.getTitle()).isEqualTo("Updated Title");
        assertThat(updated.getAuthors()).isEqualTo("Updated Authors");
        assertThat(updated.getReviewStatus()).isEqualTo(ReviewStatus.SUBMITTED);
        verify(paperRepository).save(existing);
    }

    @Test
    @DisplayName("updatePaper should transition REVISION_REQUESTED paper to UNDER_REVIEW upon edit")
    void shouldUpdateAndTransitionStatusWhenPaperInRevisionRequested() {
        Paper existing = new Paper();
        existing.setId(2L);
        existing.setTitle("Initial Title");
        existing.setSubmitterUsername("researcher");
        existing.setReviewStatus(ReviewStatus.REVISION_REQUESTED);

        when(paperRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(paperRepository.save(any(Paper.class))).thenAnswer(inv -> inv.getArgument(0));

        PaperSubmissionForm form = PaperSubmissionForm.fromPaper(existing);
        form.setTitle("Revised Title Addressing Reviewer Feedback");

        Paper updated = paperService.updatePaper(2L, form, "researcher");

        assertThat(updated.getTitle()).isEqualTo("Revised Title Addressing Reviewer Feedback");
        assertThat(updated.getReviewStatus()).isEqualTo(ReviewStatus.UNDER_REVIEW);
    }

    @Test
    @DisplayName("updatePaper should reject edits from non-author with IllegalStateException")
    void shouldRejectUpdateWhenCallerIsNotAuthor() {
        Paper existing = new Paper();
        existing.setId(3L);
        existing.setSubmitterUsername("original_author");
        existing.setReviewStatus(ReviewStatus.SUBMITTED);

        when(paperRepository.findById(3L)).thenReturn(Optional.of(existing));

        PaperSubmissionForm form = new PaperSubmissionForm();
        form.setTitle("Hacked Title");

        assertThatThrownBy(() -> paperService.updatePaper(3L, form, "different_user"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Unauthorized");
    }

    @Test
    @DisplayName("updatePaper should reject edits for paper in UNDER_REVIEW, ACCEPTED, or REJECTED status")
    void shouldRejectUpdateWhenStatusIsNotEditable() {
        Paper acceptedPaper = new Paper();
        acceptedPaper.setId(4L);
        acceptedPaper.setSubmitterUsername("researcher");
        acceptedPaper.setReviewStatus(ReviewStatus.ACCEPTED);

        when(paperRepository.findById(4L)).thenReturn(Optional.of(acceptedPaper));

        PaperSubmissionForm form = new PaperSubmissionForm();
        form.setTitle("Update to Accepted Paper");

        assertThatThrownBy(() -> paperService.updatePaper(4L, form, "researcher"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot edit paper in status");
    }

    @Test
    @DisplayName("updatePaper should throw IllegalArgumentException when paper does not exist")
    void shouldThrowWhenPaperDoesNotExist() {
        when(paperRepository.findById(999L)).thenReturn(Optional.empty());

        PaperSubmissionForm form = new PaperSubmissionForm();
        form.setTitle("Non-existent paper update");

        assertThatThrownBy(() -> paperService.updatePaper(999L, form, "researcher"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Paper not found");
    }
}
