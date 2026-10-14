package com.muawiyah.repository.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security 6 configuration establishing role-based authentication and URL authorization.
 *
 * ROLES AND USERS (Development / Academic Prototype):
 * 1. researcher: ROLE_RESEARCHER -> can submit papers and edit own submissions.
 * 2. reviewer:   ROLE_REVIEWER   -> can evaluate papers and transition review status.
 * 3. admin:      ROLE_ADMIN      -> administrative oversight and metrics dashboard.
 *
 * PUBLIC ENDPOINTS:
 * - Home (/), Catalog (/papers), and Paper Detail (/papers/{id}) are open to all visitors
 *   without requiring authentication.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode("password");

        UserDetails researcher = User.builder()
                .username("researcher")
                .password(encodedPassword)
                .roles("RESEARCHER")
                .build();

        UserDetails reviewer = User.builder()
                .username("reviewer")
                .password(encodedPassword)
                .roles("REVIEWER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(encodedPassword)
                .roles("ADMIN", "REVIEWER", "RESEARCHER")
                .build();

        return new InMemoryUserDetailsManager(researcher, reviewer, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Static resources & H2 development database console
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/error").permitAll()

                // Public visitor pages (no login required)
                .requestMatchers(HttpMethod.GET, "/", "/papers", "/papers/{id:[0-9]+}").permitAll()
                .requestMatchers("/login").permitAll()

                // Researcher-only capabilities
                .requestMatchers("/papers/submit").hasRole("RESEARCHER")
                .requestMatchers("/papers/*/edit").hasRole("RESEARCHER")

                // Reviewer capabilities (Task 10)
                .requestMatchers("/papers/*/review", "/papers/*/status").hasRole("REVIEWER")

                // Admin capabilities (Task 11)
                .requestMatchers("/admin/**", "/dashboard/**").hasRole("ADMIN")

                // Any other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/papers", false)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .csrf(csrf -> csrf
                // Disable CSRF solely for H2 console frame inspection
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
            )
            .headers(headers -> headers
                // Allow H2 console to load inside iframes from same origin
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}
