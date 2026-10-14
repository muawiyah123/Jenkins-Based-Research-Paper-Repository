package com.muawiyah.repository.repository;

import com.muawiyah.repository.model.Paper;
import com.muawiyah.repository.model.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository interface for Paper entities.
 * Provides standard CRUD and custom search/filter methods.
 */
@Repository
public interface PaperRepository extends JpaRepository<Paper, Long> {

    /**
     * Finds papers by exact review status with pagination.
     */
    Page<Paper> findByReviewStatus(ReviewStatus reviewStatus, Pageable pageable);

    /**
     * Counts papers matching a given review status for dashboard metrics.
     */
    long countByReviewStatus(ReviewStatus reviewStatus);

    /**
     * Finds all papers submitted by a specific user.
     */
    List<Paper> findBySubmitterUsername(String submitterUsername);

    /**
     * Case-insensitive partial search across title, authors, and keywords.
     */
    @Query("SELECT p FROM Paper p WHERE " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.authors) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.keywords) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Paper> searchPapers(@Param("query") String query, Pageable pageable);

    /**
     * Case-insensitive partial search with status filter.
     */
    @Query("SELECT p FROM Paper p WHERE " +
           "p.reviewStatus = :status AND (" +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.authors) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.keywords) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Paper> searchPapersByStatus(@Param("query") String query,
                                     @Param("status") ReviewStatus status,
                                     Pageable pageable);
}
