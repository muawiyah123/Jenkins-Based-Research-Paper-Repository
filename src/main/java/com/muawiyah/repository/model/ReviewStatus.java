package com.muawiyah.repository.model;

/**
 * Enumeration representing the five peer-review lifecycle states
 * for a research paper in the repository.
 *
 * State Transitions:
 * SUBMITTED -> UNDER_REVIEW -> ACCEPTED (Terminal)
 *                           -> REJECTED (Terminal)
 *                           -> REVISION_REQUESTED -> UNDER_REVIEW
 */
public enum ReviewStatus {
    SUBMITTED("Submitted", "Paper has been submitted and is awaiting assignment."),
    UNDER_REVIEW("Under Review", "Paper is actively being evaluated by reviewers."),
    REVISION_REQUESTED("Revision Requested", "Reviewer has requested revisions from the author."),
    ACCEPTED("Accepted", "Paper has been formally accepted for publication."),
    REJECTED("Rejected", "Paper has been rejected.");

    private final String displayName;
    private final String description;

    ReviewStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Checks if this status is terminal (immutable state where no further transitions are allowed).
     *
     * @return true if ACCEPTED or REJECTED
     */
    public boolean isTerminal() {
        return this == ACCEPTED || this == REJECTED;
    }

    /**
     * Returns the Bootstrap badge CSS class for this status.
     */
    public String getBadgeClass() {
        return switch (this) {
            case ACCEPTED -> "bg-success text-white";
            case REJECTED -> "bg-danger text-white";
            case UNDER_REVIEW -> "bg-warning text-dark";
            case REVISION_REQUESTED -> "bg-secondary text-white";
            case SUBMITTED -> "bg-primary text-white";
        };
    }

    /**
     * State Machine Rule Validator:
     * Asserts whether moving from this status to target status is valid.
     */
    public boolean canTransitionTo(ReviewStatus target) {
        if (target == null || this == target) {
            return false;
        }
        return switch (this) {
            case SUBMITTED -> target == UNDER_REVIEW;
            case UNDER_REVIEW -> target == ACCEPTED || target == REJECTED || target == REVISION_REQUESTED;
            case REVISION_REQUESTED -> target == UNDER_REVIEW;
            case ACCEPTED, REJECTED -> false;
        };
    }

    /**
     * Returns the set of valid next states permitted from this status.
     */
    public java.util.Set<ReviewStatus> getPermittedNextStates() {
        return switch (this) {
            case SUBMITTED -> java.util.Set.of(UNDER_REVIEW);
            case UNDER_REVIEW -> java.util.Set.of(ACCEPTED, REJECTED, REVISION_REQUESTED);
            case REVISION_REQUESTED -> java.util.Set.of(UNDER_REVIEW);
            case ACCEPTED, REJECTED -> java.util.Collections.emptySet();
        };
    }
}
