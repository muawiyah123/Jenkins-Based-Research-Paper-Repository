package com.muawiyah.repository.controller;

import com.muawiyah.repository.dto.PaperSubmissionForm;
import com.muawiyah.repository.model.Paper;
import com.muawiyah.repository.service.PaperService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

/**
 * MVC Controller handling the research paper submission, catalog, and detail workflows.
 *
 * HOW SPRING MVC WORKS (beginner explanation):
 * 1. The browser sends a GET /papers/submit request.
 * 2. Spring routes it to showSubmitForm().
 * 3. The method adds an empty PaperSubmissionForm to the model and returns the
 *    template name "papers/submit".
 * 4. Thymeleaf finds src/main/resources/templates/papers/submit.html and renders it.
 * 5. The browser displays the HTML form.
 *
 * On form POST:
 * 1. Spring binds all form fields to PaperSubmissionForm automatically.
 * 2. @Valid triggers Bean Validation (size, notBlank, email etc.).
 * 3. If validation fails, BindingResult.hasErrors() is true — we re-render the form
 *    with error messages visible. NO database write occurs.
 * 4. If validation passes, the service saves the paper and we redirect to the detail page.
 */
@Controller
@RequestMapping("/papers")
public class PaperController {

    private static final int PAGE_SIZE = 10;

    private final PaperService paperService;

    public PaperController(PaperService paperService) {
        this.paperService = paperService;
    }

    // ─── Catalog ──────────────────────────────────────────────────────────────

    /**
     * Renders the public paper catalog with optional keyword search.
     * GET /papers                     → all papers, newest first
     * GET /papers?q=keyword           → search results
     * GET /papers?page=1              → second page of results
     * GET /papers?q=keyword&page=1    → second page of search results
     *
     * HOW PAGINATION WORKS:
     * PageRequest.of(page, size, sort) tells Spring Data to execute a SELECT
     * with LIMIT (size) and OFFSET (page * size). The Page<Paper> it returns
     * also contains total count, total pages, and navigation flags that
     * Thymeleaf uses to render the pagination controls.
     */
    @GetMapping
    public String showCatalog(
            @RequestParam(value = "q",    required = false)  String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(
                Math.max(page, 0), PAGE_SIZE,
                Sort.by("createdAt").descending());

        String trimmedQuery = (query != null && !query.isBlank()) ? query.trim() : "";
        Page<Paper> papers = trimmedQuery.isEmpty()
                ? paperService.findAll(pageable)
                : paperService.search(trimmedQuery, pageable);

        model.addAttribute("papers",    papers);
        model.addAttribute("query",     trimmedQuery);
        model.addAttribute("pageTitle", "Research Paper Catalog");
        return "papers/catalog";
    }

    // ─── Detail ───────────────────────────────────────────────────────────────

    /**
     * Renders the detail page for a single paper.
     * GET /papers/{id}
     *
     * If the paper does not exist (wrong ID or deleted), the user is redirected
     * to the catalog with a clear error flash message instead of seeing a
     * generic 500 error page.
     */
    @GetMapping("/{id}")
    public String showPaperDetail(@PathVariable Long id,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Optional<Paper> paperOpt = paperService.findById(id);
        if (paperOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Paper with ID " + id + " was not found in the repository.");
            return "redirect:/papers";
        }
        Paper paper = paperOpt.get();
        model.addAttribute("paper",     paper);
        model.addAttribute("pageTitle", paper.getTitle());
        return "papers/detail";
    }

    // ─── Submission ───────────────────────────────────────────────────────────

    /**
     * Renders the empty submission form.
     * GET /papers/submit
     */
    @GetMapping("/submit")
    public String showSubmitForm(Model model) {
        model.addAttribute("paperForm", new PaperSubmissionForm());
        model.addAttribute("pageTitle", "Submit Research Paper");
        return "papers/submit";
    }

    /**
     * Handles form submission.
     * POST /papers/submit
     *
     * @param form       bound and validated form data
     * @param result     contains any field validation errors
     * @param principal  the currently authenticated user (null until Task 9 adds security)
     * @param redirectAttributes used to pass a flash message through the redirect
     */
    @PostMapping("/submit")
    public String handleSubmit(@Valid @ModelAttribute("paperForm") PaperSubmissionForm form,
                               BindingResult result,
                               Principal principal,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        // If any field failed validation, re-render the form with error messages.
        // The browser stays on the form page; nothing is saved.
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Submit Research Paper");
            return "papers/submit";
        }

        // Security note:
        // Principal is null here because authentication (Task 9) is not yet implemented.
        // We use the placeholder "researcher" until Spring Security is configured.
        // TODO Task 9: replace placeholder with principal.getName()
        String submitterUsername = (principal != null) ? principal.getName() : "researcher";

        Paper saved = paperService.submitPaper(form, submitterUsername);

        // Flash attributes survive a redirect and are then removed automatically.
        redirectAttributes.addFlashAttribute("successMessage",
                "Paper submitted successfully! Your paper ID is #" + saved.getId() +
                ". It will appear in the catalog once it is accepted by a reviewer.");

        // Redirect to the detail page so the researcher can review what was saved.
        return "redirect:/papers/" + saved.getId();
    }
}
