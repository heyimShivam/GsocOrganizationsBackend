package com.organization.gsoc.Config;

import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import com.organization.gsoc.Service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            final GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler,
            final GoogleOAuth2FailureHandler googleOAuth2FailureHandler,
            final RestAuthenticationEntryPoint restAuthenticationEntryPoint
    ) throws Exception {

        http
                // Enable CORS for Spring Security
                .cors(cors -> {})

                // CSRF disabled because we are using session authentication
                // with our current API setup
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.IF_REQUIRED
                        )
                ).exceptionHandling(exception -> exception
                        .defaultAuthenticationEntryPointFor(
                                restAuthenticationEntryPoint,
                                PathPatternRequestMatcher
                                        .withDefaults()
                                        .matcher("/api/**")
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        /*
                         * IMPORTANT:
                         * Browser sends OPTIONS before cross-origin
                         * POST/DELETE/PATCH requests.
                         *
                         * Preflight itself does not need authentication.
                         */
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        /*
                         * PUBLIC ENDPOINTS
                         */
                        .requestMatchers(

                                // Authentication
                                "/api/auth/signup",
                                "/api/auth/login",
                                "/api/auth/logout",
                                "/api/auth/verify-email",
                                "/api/auth/resend-verification",
//
                                "/api/user-profile/**",
                                "/api/user-profile",

                                // OAuth
                                "/oauth2/**",
                                "/login/**",

                                // Contact Us
                                "/api/contact-us",

                                // Organizations
                                "/api/organizations",
                                "/api/organizations/**",

                                // Repositories
                                "/api/repositories",
                                "/api/repositories/**",

                                // Topics
                                "/api/topics",
                                "/api/topics/**",

                                // Categories
                                "/api/categories",
                                "/api/categories/**",

                                // Technologies
                                "/api/technologies",
                                "/api/technologies/**",

                                // Organization filters
                                "/api/all-filters",

                                // GSoC projects
                                "/api/projects",
                                "/api/projects/**",

                                // Contributors
                                "/api/organizations/*/contributors"

                        ).permitAll()

                        /*
                         * ADMIN ENDPOINTS
                         */
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        /*
                         * EVERYTHING ELSE REQUIRES LOGIN
                         *
                         * This includes:
                         *
                         * GET    /api/auth/me
                         * PATCH  /api/auth/me
                         * GET    /api/auth/me/bookmarks
                         * POST   /api/auth/me/bookmarks/{id}
                         * DELETE /api/auth/me/bookmarks/{id}
                         */
                        .anyRequest()
                        .authenticated()
                )

                /*
                 * GOOGLE OAUTH2
                 */
                .oauth2Login(oauth -> oauth
                        .successHandler(
                                googleOAuth2SuccessHandler
                        )
                        .failureHandler(
                                googleOAuth2FailureHandler
                        )
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }
}