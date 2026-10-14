package com.muawiyah.repository.service.impl;

import com.muawiyah.repository.dto.PaperSubmissionForm;
import com.muawiyah.repository.model.Paper;
import com.muawiyah.repository.model.ReviewStatus;
import com.muawiyah.repository.repository.PaperRepository;
import com.muawiyah.repository.service.PaperService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
