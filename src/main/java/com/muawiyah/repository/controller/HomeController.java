package com.muawiyah.repository.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the public home page and foundational navigation scaffold.
 * Fulfills FR-05 (Public Navigation Scaffold) and US-001 (Foundation Scaffold).
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("projectTitle", "Jenkins-Based Research Paper Repository");
        model.addAttribute("studentName", "Muawiyah Narvel");
        model.addAttribute("rollNumber", "23108A0020");
        model.addAttribute("currentPhase", "Week 3 — Foundation & Application Scaffold");
        return "index";
    }
}
