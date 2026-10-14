package com.muawiyah.repository.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global Exception Handler for centralized error management (Task 12, US-010).
 * Prevents unhandled stack trace leakage and renders styled user-friendly error views.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles unmapped resource/route requests by rendering the custom 404 template.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(NoResourceFoundException ex, Model model) {
        log.warn("Resource not found: {}", ex.getResourcePath());
        model.addAttribute("status", 404);
        model.addAttribute("error", "Page Not Found");
        model.addAttribute("message", "The requested URL was not found on this server.");
        return "error/404";
    }

    /**
     * Handles invalid arguments / entity lookup failures with HTTP 404.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        log.warn("Invalid entity request: {}", ex.getMessage());
        model.addAttribute("status", 404);
        model.addAttribute("error", "Entity Not Found");
        model.addAttribute("message", ex.getMessage());
        return "error/404";
    }

    /**
     * Re-throws Spring Security AccessDeniedException to let SecurityFilterChain
     * handle access denied challenges consistently.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDenied(AccessDeniedException ex) {
        throw ex;
    }

    /**
     * Catches all unhandled exceptions and safely renders the 500 error view without stack trace leaks.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unhandled internal server exception: {}", ex.getMessage(), ex);
        model.addAttribute("status", 500);
        model.addAttribute("error", "Internal Server Error");
        model.addAttribute("message", "An unexpected condition occurred. Our administrators have been notified.");
        return "error/500";
    }
}
