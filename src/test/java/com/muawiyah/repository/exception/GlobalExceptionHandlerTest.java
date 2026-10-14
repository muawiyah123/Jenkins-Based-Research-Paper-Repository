package com.muawiyah.repository.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("handleResourceNotFound should return error/404 view with 404 status in model")
    void shouldHandleResourceNotFound() {
        NoResourceFoundException ex = new NoResourceFoundException(HttpMethod.GET, "/non-existent-page");
        Model model = new ConcurrentModel();

        String view = handler.handleResourceNotFound(ex, model);

        assertThat(view).isEqualTo("error/404");
        assertThat(model.getAttribute("status")).isEqualTo(404);
        assertThat(model.getAttribute("error")).isEqualTo("Page Not Found");
    }

    @Test
    @DisplayName("handleIllegalArgument should return error/404 view with error message in model")
    void shouldHandleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Paper #999 not found");
        Model model = new ConcurrentModel();

        String view = handler.handleIllegalArgument(ex, model);

        assertThat(view).isEqualTo("error/404");
        assertThat(model.getAttribute("status")).isEqualTo(404);
        assertThat(model.getAttribute("message")).isEqualTo("Paper #999 not found");
    }

    @Test
    @DisplayName("handleGenericException should return error/500 view without leaking stack traces")
    void shouldHandleGenericException() {
        RuntimeException ex = new RuntimeException("Database timeout failure");
        Model model = new ConcurrentModel();

        String view = handler.handleGenericException(ex, model);

        assertThat(view).isEqualTo("error/500");
        assertThat(model.getAttribute("status")).isEqualTo(500);
        assertThat(model.getAttribute("error")).isEqualTo("Internal Server Error");
    }

    @Test
    @DisplayName("handleAccessDenied should rethrow AccessDeniedException for Spring Security")
    void shouldRethrowAccessDeniedException() {
        AccessDeniedException ex = new AccessDeniedException("Access is denied");

        assertThatThrownBy(() -> handler.handleAccessDenied(ex))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Access is denied");
    }
}
