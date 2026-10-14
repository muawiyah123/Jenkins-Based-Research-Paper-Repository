package com.muawiyah.repository.service;

import com.muawiyah.repository.dto.PaperSubmissionForm;
import com.muawiyah.repository.model.Paper;
import com.muawiyah.repository.model.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service interface for research paper business operations.
 *
 * WHY AN INTERFACE?
 * Declaring behaviour in an interface and providing a separate implementation
 * class (PaperServiceImpl) allows us to:
 * 1. Unit test the controller by injecting a mock PaperService without touching the database.
 * 2. Replace the implementation in future (e.g. swap H2 for PostgreSQL) without
 *    changing any controller or test code.
 *
 * Methods are added task by task as features are implemented.
 */
public interface PaperService {

    /**
     * Submits a new research paper to the repository.
     *
     * The review status is set to SUBMITTED automatically.
     * The submitterUsername is assigned server-side from the authenticated
     * principal — it is never accepted from the HTML form.
     *
     * @param form              validated form data from the researcher
     * @param submitterUsername username of the authenticated researcher
     * @return the persisted Paper entity with generated ID and timestamps
     */
    Paper submitPaper(PaperSubmissionForm form, String submitterUsername);

    /**
     * Returns a paginated list of all papers, newest first.
     * Used by the public catalog when no search query is present.
     *
     * @param pageable page number, size, and sort direction
     * @return a Spring Data Page wrapping the result list
     */
    Page<Paper> findAll(Pageable pageable);

    /**
     * Finds a single paper by its primary key.
     *
     * @param id database-generated paper ID
     * @return Optional containing the paper, or empty if not found
     */
    Optional<Paper> findById(Long id);

    /**
     * Searches papers by keyword across title, authors, and keywords fields.
     * The search is case-insensitive and uses a substring match.
     *
     * @param keyword  search term entered by the user
     * @param pageable page number, size, and sort direction
     * @return a Page of matching papers
     */
    Page<Paper> search(String keyword, Pageable pageable);

    /**
     * Updates an existing research paper's metadata and enforces lifecycle transition rules.
     *
     * Authorization and State Rules:
     * 1. Paper must exist (or throws IllegalArgumentException).
     * 2. Author verification: caller must match submitterUsername (or throws IllegalStateException).
     * 3. State lock: Only SUBMITTED or REVISION_REQUESTED papers may be edited. Papers
     *    in UNDER_REVIEW, ACCEPTED, or REJECTED status throw IllegalStateException.
     * 4. Resubmission transition: If the paper is in REVISION_REQUESTED, saving revisions
     *    automatically transitions reviewStatus to UNDER_REVIEW.
     *
     * @param id              database ID of the paper to edit
     * @param form            validated form DTO containing updated fields
     * @param currentUsername username of the user attempting the edit
     * @return the persisted updated Paper entity
     */
    Paper updatePaper(Long id, PaperSubmissionForm form, String currentUsername);

    /**
     * Executes a peer-review lifecycle state transition.
     * Enforces domain state machine rules:
     * - Rejects invalid jumps (e.g. SUBMITTED -> ACCEPTED).
     * - Rejects transitions out of immutable terminal states (ACCEPTED, REJECTED).
     *
     * @param paperId          ID of the paper
     * @param newStatus        target ReviewStatus to transition to
     * @param reviewerUsername authenticated username of the reviewer
     * @return the updated Paper entity
     */
    Paper updateReviewStatus(Long paperId, ReviewStatus newStatus, String reviewerUsername);

    /**
     * Returns a paginated list of papers matching a specific review status.
     *
     * @param status   the review status to filter by
     * @param pageable page number, size, and sorting
     * @return a Page of matching papers
     */
    Page<Paper> findByReviewStatus(ReviewStatus status, Pageable pageable);

    /**
     * Counts the total number of research papers stored in the repository.
     *
     * @return total count of all papers
     */
    long countTotalPapers();

    /**
     * Aggregates paper counts across all five review statuses for the dashboard.
     *
     * @return map of ReviewStatus to paper count
     */
    java.util.Map<ReviewStatus, Long> getReviewStatusCounts();

    /**
     * Retrieves the most recent papers for dashboard display.
     *
     * @param limit maximum number of papers to retrieve
     * @return list of recent Paper entities
     */
    java.util.List<Paper> getRecentSubmissions(int limit);
}
