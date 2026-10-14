package com.muawiyah.repository.service;

import com.muawiyah.repository.dto.PaperSubmissionForm;
import com.muawiyah.repository.model.Paper;

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
 * Methods will be added here task by task as features are implemented.
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
}
