package com.muawiyah.repository.repository;

import com.muawiyah.repository.model.Paper;
import com.muawiyah.repository.model.ReviewStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PaperRepositoryTest {

    @Autowired
    private PaperRepository paperRepository;

    @Test
    @DisplayName("Should persist and retrieve paper with auto-generated ID and default SUBMITTED status")
    void shouldPersistAndRetrievePaper() {
        Paper paper = new Paper(
                "Test Paper on Distributed Systems",
                "Leslie Lamport",
                "A comprehensive study of consensus algorithms and replicated state machines.",
                "Consensus, Paxos, Raft, Distributed Systems",
                2024,
                "https://doi.org/10.1000/test1",
                "leslie@systems.org",
                "lamport"
        );

        Paper saved = paperRepository.save(paper);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getReviewStatus()).isEqualTo(ReviewStatus.SUBMITTED);
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();

        Optional<Paper> found = paperRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Paper on Distributed Systems");
    }

    @Test
    @DisplayName("Should find papers by review status with pagination")
    void shouldFindByReviewStatus() {
        Paper p1 = new Paper("Paper A", "Author A", "Abstract A with sufficient length here.", "TagA", 2024, null, "a@a.com", "userA");
        p1.setReviewStatus(ReviewStatus.ACCEPTED);
        paperRepository.save(p1);

        Paper p2 = new Paper("Paper B", "Author B", "Abstract B with sufficient length here.", "TagB", 2024, null, "b@b.com", "userB");
        p2.setReviewStatus(ReviewStatus.UNDER_REVIEW);
        paperRepository.save(p2);

        Page<Paper> accepted = paperRepository.findByReviewStatus(ReviewStatus.ACCEPTED, PageRequest.of(0, 10));
        assertThat(accepted.getContent()).anyMatch(p -> p.getTitle().equals("Paper A"));
        assertThat(accepted.getContent()).noneMatch(p -> p.getTitle().equals("Paper B"));
    }

    @Test
    @DisplayName("Should count papers by review status for dashboard metrics")
    void shouldCountByReviewStatus() {
        long initialCount = paperRepository.countByReviewStatus(ReviewStatus.REJECTED);

        Paper rejected = new Paper("Rejected Study", "Author R", "Abstract for rejected paper with sufficient length.", "TagR", 2023, null, "r@r.com", "userR");
        rejected.setReviewStatus(ReviewStatus.REJECTED);
        paperRepository.save(rejected);

        long updatedCount = paperRepository.countByReviewStatus(ReviewStatus.REJECTED);
        assertThat(updatedCount).isEqualTo(initialCount + 1);
    }

    @Test
    @DisplayName("Should find papers by submitter username")
    void shouldFindBySubmitterUsername() {
        Paper paper = new Paper("User Specific Paper", "Author S", "Abstract for user paper with sufficient length.", "TagS", 2025, null, "s@s.com", "specialUser");
        paperRepository.save(paper);

        List<Paper> userPapers = paperRepository.findBySubmitterUsername("specialUser");
        assertThat(userPapers).hasSize(1);
        assertThat(userPapers.get(0).getTitle()).isEqualTo("User Specific Paper");
    }

    @Test
    @DisplayName("Should perform case-insensitive search across title, authors, and keywords")
    void shouldSearchAcrossFields() {
        Paper paper = new Paper(
                "Modern DevSecOps Practices in Kubernetes",
                "Linus Torvalds",
                "Container security and supply chain integrity in modern platforms.",
                "DevSecOps, Containers, Linux",
                2025,
                null,
                "linus@kernel.org",
                "linus"
        );
        paperRepository.save(paper);

        // Search by title fragment (lowercase)
        Page<Paper> byTitle = paperRepository.searchPapers("kubernetes", PageRequest.of(0, 10));
        assertThat(byTitle.getContent()).anyMatch(p -> p.getTitle().contains("Kubernetes"));

        // Search by author fragment (lowercase)
        Page<Paper> byAuthor = paperRepository.searchPapers("torvalds", PageRequest.of(0, 10));
        assertThat(byAuthor.getContent()).anyMatch(p -> p.getAuthors().contains("Torvalds"));

        // Search by keyword fragment (lowercase)
        Page<Paper> byKeyword = paperRepository.searchPapers("containers", PageRequest.of(0, 10));
        assertThat(byKeyword.getContent()).anyMatch(p -> p.getKeywords().contains("Containers"));
    }
}
