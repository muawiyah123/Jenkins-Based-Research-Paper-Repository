package com.muawiyah.repository.controller;

import com.muawiyah.repository.dto.PaperSubmissionForm;
import com.muawiyah.repository.model.Paper;
import com.muawiyah.repository.service.PaperService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

/**
 * MVC Controller handling the research paper submission workflow.
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
 * 4. If validation passes, the service saves the paper and we redirect to home.
 */
@Controller
@RequestMapping("/papers")
public class PaperController {

    private final PaperService paperService;

    public PaperController(PaperService paperService) {
        this.paperService = paperService;
    }

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
        // After Task 9, replace this with: principal.getName()
        // TODO Task 9: replace placeholder with principal.getName()
        String submitterUsername = (principal != null) ? principal.getName() : "researcher";

        Paper saved = paperService.submitPaper(form, submitterUsername);

        // Flash attributes survive a redirect and are then removed automatically.
        redirectAttributes.addFlashAttribute("successMessage",
                "Paper \"" + saved.getTitle() + "\" has been submitted successfully (ID: " + saved.getId() + ").");

        // Redirect to home. When Task 7 builds the paper detail page,
        // this will change to: "redirect:/papers/" + saved.getId()
        // TODO Task 7: redirect to paper detail page once catalog is built
        return "redirect:/";
    }
}
