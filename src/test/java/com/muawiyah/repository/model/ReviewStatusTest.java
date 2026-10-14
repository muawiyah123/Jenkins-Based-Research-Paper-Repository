package com.muawiyah.repository.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewStatusTest {

    @Test
    @DisplayName("SUBMITTED can only transition to UNDER_REVIEW")
    void submittedCanOnlyTransitionToUnderReview() {
        assertThat(ReviewStatus.SUBMITTED.canTransitionTo(ReviewStatus.UNDER_REVIEW)).isTrue();
        assertThat(ReviewStatus.SUBMITTED.canTransitionTo(ReviewStatus.ACCEPTED)).isFalse();
        assertThat(ReviewStatus.SUBMITTED.canTransitionTo(ReviewStatus.REJECTED)).isFalse();
        assertThat(ReviewStatus.SUBMITTED.canTransitionTo(ReviewStatus.REVISION_REQUESTED)).isFalse();
        assertThat(ReviewStatus.SUBMITTED.canTransitionTo(ReviewStatus.SUBMITTED)).isFalse();
        assertThat(ReviewStatus.SUBMITTED.canTransitionTo(null)).isFalse();

        assertThat(ReviewStatus.SUBMITTED.getPermittedNextStates())
                .containsExactly(ReviewStatus.UNDER_REVIEW);
    }

    @Test
    @DisplayName("UNDER_REVIEW can transition to ACCEPTED, REJECTED, or REVISION_REQUESTED")
    void underReviewTransitions() {
        assertThat(ReviewStatus.UNDER_REVIEW.canTransitionTo(ReviewStatus.ACCEPTED)).isTrue();
        assertThat(ReviewStatus.UNDER_REVIEW.canTransitionTo(ReviewStatus.REJECTED)).isTrue();
        assertThat(ReviewStatus.UNDER_REVIEW.canTransitionTo(ReviewStatus.REVISION_REQUESTED)).isTrue();
        assertThat(ReviewStatus.UNDER_REVIEW.canTransitionTo(ReviewStatus.SUBMITTED)).isFalse();
        assertThat(ReviewStatus.UNDER_REVIEW.canTransitionTo(ReviewStatus.UNDER_REVIEW)).isFalse();

        assertThat(ReviewStatus.UNDER_REVIEW.getPermittedNextStates())
                .containsExactlyInAnyOrder(ReviewStatus.ACCEPTED, ReviewStatus.REJECTED, ReviewStatus.REVISION_REQUESTED);
    }

    @Test
    @DisplayName("REVISION_REQUESTED can only transition back to UNDER_REVIEW")
    void revisionRequestedTransitions() {
        assertThat(ReviewStatus.REVISION_REQUESTED.canTransitionTo(ReviewStatus.UNDER_REVIEW)).isTrue();
        assertThat(ReviewStatus.REVISION_REQUESTED.canTransitionTo(ReviewStatus.ACCEPTED)).isFalse();
        assertThat(ReviewStatus.REVISION_REQUESTED.canTransitionTo(ReviewStatus.REJECTED)).isFalse();
        assertThat(ReviewStatus.REVISION_REQUESTED.canTransitionTo(ReviewStatus.SUBMITTED)).isFalse();

        assertThat(ReviewStatus.REVISION_REQUESTED.getPermittedNextStates())
                .containsExactly(ReviewStatus.UNDER_REVIEW);
    }

    @ParameterizedTest
    @EnumSource(value = ReviewStatus.class, names = {"ACCEPTED", "REJECTED"})
    @DisplayName("Terminal states cannot transition to any state and have empty permitted states")
    void terminalStatesCannotTransition(ReviewStatus terminal) {
        assertThat(terminal.isTerminal()).isTrue();
        assertThat(terminal.getPermittedNextStates()).isEmpty();

        for (ReviewStatus target : ReviewStatus.values()) {
            assertThat(terminal.canTransitionTo(target)).isFalse();
        }
    }

    @ParameterizedTest
    @EnumSource(value = ReviewStatus.class, names = {"SUBMITTED", "UNDER_REVIEW", "REVISION_REQUESTED"})
    @DisplayName("Non-terminal states are correctly identified")
    void nonTerminalStates(ReviewStatus status) {
        assertThat(status.isTerminal()).isFalse();
    }
}
