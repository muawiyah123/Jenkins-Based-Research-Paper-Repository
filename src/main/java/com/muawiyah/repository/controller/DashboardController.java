package com.muawiyah.repository.controller;

import com.muawiyah.repository.model.Paper;
import com.muawiyah.repository.model.ReviewStatus;
import com.muawiyah.repository.service.PaperService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * Controller handling the Repository Metrics Dashboard (Task 11, US-008).
 * Displays real-time aggregate counts for all peer-review lifecycle states
 * and total papers submitted to the repository.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final PaperService paperService;

    public DashboardController(PaperService paperService) {
        this.paperService = paperService;
    }

    /**
     * Renders the administrative metrics dashboard.
     * Accessible by any authenticated role (researcher, reviewer, admin).
     *
     * @param model Spring MVC model for view rendering
     * @return Thymeleaf view template name "dashboard"
     */
    @GetMapping
    public String showDashboard(Model model) {
        long totalPapers = paperService.countTotalPapers();
        Map<ReviewStatus, Long> statusCounts = paperService.getReviewStatusCounts();
        List<Paper> recentPapers = paperService.getRecentSubmissions(5);

        model.addAttribute("totalPapers", totalPapers);
        model.addAttribute("statusCounts", statusCounts);
        model.addAttribute("submittedCount", statusCounts.getOrDefault(ReviewStatus.SUBMITTED, 0L));
        model.addAttribute("underReviewCount", statusCounts.getOrDefault(ReviewStatus.UNDER_REVIEW, 0L));
        model.addAttribute("revisionRequestedCount", statusCounts.getOrDefault(ReviewStatus.REVISION_REQUESTED, 0L));
        model.addAttribute("acceptedCount", statusCounts.getOrDefault(ReviewStatus.ACCEPTED, 0L));
        model.addAttribute("rejectedCount", statusCounts.getOrDefault(ReviewStatus.REJECTED, 0L));
        model.addAttribute("recentPapers", recentPapers);
        model.addAttribute("pageTitle", "Repository Metrics Dashboard");

        return "dashboard";
    }
}
