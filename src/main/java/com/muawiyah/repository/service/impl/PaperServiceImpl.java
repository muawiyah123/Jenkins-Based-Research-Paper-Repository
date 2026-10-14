package com.muawiyah.repository.service.impl;

import com.muawiyah.repository.dto.PaperSubmissionForm;
import com.muawiyah.repository.model.Paper;
import com.muawiyah.repository.model.ReviewStatus;
import com.muawiyah.repository.repository.PaperRepository;
import com.muawiyah.repository.service.PaperService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of PaperService.
 *
 * @Service tells Spring to create one instance of this class and
 *          manage it as a Spring bean (singleton by default).
 *
 * @Transactional means any database operation in this class is wrapped
 *               in a transaction automatically — if something fails,
 *               the database rolls back to a consistent state.
 */
@Service
@Transactional
public class PaperServiceImpl implements PaperService {

    private final PaperRepository paperRepository;

    /**
     * Constructor injection is preferred over @Autowired field injection
     * because it makes dependencies explicit and simplifies unit testing.
     */
    public PaperServiceImpl(PaperRepository paperRepository) {
        this.paperRepository = paperRepository;
    }

    /**
     * {@inheritDoc}
     *
     * Converts the form DTO into a Paper entity, assigns server-controlled
     * fields (status, username), and persists it.
     *
     * Security note: submitterUsername is passed in from the controller
     * using the authenticated principal, NOT from any HTML form input.
     */
    @Override
    public Paper submitPaper(PaperSubmissionForm form, String submitterUsername) {
        Paper paper = new Paper();
        paper.setTitle(form.getTitle());
        paper.setAuthors(form.getAuthors());
        paper.setAbstractText(form.getAbstractText());
        paper.setKeywords(form.getKeywords());
        paper.setPublicationYear(form.getPublicationYear());
        paper.setDoiUrl(form.getDoiUrl());
        paper.setSubmitterEmail(form.getSubmitterEmail());
        // Assigned server-side; never from form input
        paper.setSubmitterUsername(submitterUsername);
        // Always starts as SUBMITTED
        paper.setReviewStatus(ReviewStatus.SUBMITTED);
        return paperRepository.save(paper);
    }

    /**
     * {@inheritDoc}
     *
     * readOnly = true is a performance optimisation for queries:
     * Hibernate skips the dirty-checking snapshot, reducing memory usage.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Paper> findAll(Pageable pageable) {
        return paperRepository.findAll(pageable);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Optional<Paper> findById(Long id) {
        return paperRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     *
     * Delegates to the custom JPQL query defined in PaperRepository
     * which performs a case-insensitive substring search across three columns.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Paper> search(String keyword, Pageable pageable) {
        return paperRepository.searchByKeyword(keyword, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Paper updatePaper(Long id, PaperSubmissionForm form, String currentUsername) {
        Paper paper = paperRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paper not found with id: " + id));

        // 1. Author ownership verification
        if (currentUsername != null && !paper.getSubmitterUsername().equals(currentUsername)) {
            throw new IllegalStateException("Unauthorized: Only the author '" + paper.getSubmitterUsername() +
                    "' is permitted to edit this paper.");
        }

        // 2. Status verification: only SUBMITTED or REVISION_REQUESTED papers are editable
        ReviewStatus status = paper.getReviewStatus();
        if (status != ReviewStatus.SUBMITTED && status != ReviewStatus.REVISION_REQUESTED) {
            throw new IllegalStateException("Cannot edit paper in status: " + status);
        }

        // 3. Update editable metadata fields
        paper.setTitle(form.getTitle());
        paper.setAuthors(form.getAuthors());
        paper.setAbstractText(form.getAbstractText());
        paper.setKeywords(form.getKeywords());
        paper.setPublicationYear(form.getPublicationYear());
        paper.setDoiUrl(form.getDoiUrl());
        paper.setSubmitterEmail(form.getSubmitterEmail());

        // 4. Resubmission transition: moving from REVISION_REQUESTED to UNDER_REVIEW
        if (status == ReviewStatus.REVISION_REQUESTED) {
            paper.setReviewStatus(ReviewStatus.UNDER_REVIEW);
        }

        return paperRepository.save(paper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Paper updateReviewStatus(Long paperId, ReviewStatus newStatus, String reviewerUsername) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new IllegalArgumentException("Paper not found with id: " + paperId));

        ReviewStatus currentStatus = paper.getReviewStatus();

        // 1. Assert terminal state immutability
        if (currentStatus.isTerminal()) {
            throw new IllegalStateException("Paper #" + paperId + " is in terminal state " +
                    currentStatus + " and cannot be modified.");
        }

        // 2. Assert valid state transition via domain state machine rules
        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new IllegalStateException("Invalid state transition: Cannot move from " +
                    currentStatus + " to " + newStatus);
        }

        // 3. Apply state transition
        paper.setReviewStatus(newStatus);

        return paperRepository.save(paper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Paper> findByReviewStatus(ReviewStatus status, Pageable pageable) {
        return paperRepository.findByReviewStatus(status, pageable);
    }
}
