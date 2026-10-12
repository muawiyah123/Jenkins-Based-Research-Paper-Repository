package com.muawiyah.repository.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Integration test for HomeController.
 * Verifies that the public home page loads successfully with HTTP 200,
 * binds required model attributes, and resolves to the 'index' view.
 * Fulfills AC-001-3 (Home page response and layout verification).
 */
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHomePageLoadsSuccessfully() throws Exception {
        long startTime = System.currentTimeMillis();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("projectTitle"))
                .andExpect(model().attributeExists("studentName"))
                .andExpect(model().attributeExists("rollNumber"));

        long duration = System.currentTimeMillis() - startTime;
        // Verify provisional local latency boundary (< 500 ms)
        assertTrue(
                duration < 500,
                "Home page request exceeded the provisional 500 ms target: " + duration + "ms"
        );
    }
}
