package com.organization.gsoc.Config;

import com.organization.gsoc.Entity.UserEntity;
import com.organization.gsoc.Enums.UserRole;
import com.organization.gsoc.Repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoogleOAuth2SuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public GoogleOAuth2SuccessHandler(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauth2User =
                (OAuth2User) authentication.getPrincipal();

        String email =
                oauth2User.getAttribute("email");

        String name =
                oauth2User.getAttribute("name");

        Boolean emailVerified =
                oauth2User.getAttribute("email_verified");

        if (email == null || email.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Google account email could not be retrieved"
            );

            return;
        }

        if (!Boolean.TRUE.equals(emailVerified)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Google email is not verified"
            );

            return;
        }

        final String normalizedEmail =
                email.trim().toLowerCase();

        /*
         * Find existing user or create new Google user.
         */
        UserEntity user =
                userRepository
                        .findByEmail(normalizedEmail)
                        .orElseGet(() -> {

                            UserEntity newUser =
                                    new UserEntity();

                            newUser.setName(
                                    name != null && !name.isBlank()
                                            ? name
                                            : normalizedEmail
                            );

                            newUser.setEmail(
                                    normalizedEmail
                            );

                            // Google-only account
                            newUser.setPassword(null);

                            newUser.setRole(
                                    UserRole.USER
                            );

                            newUser.setDescription(
                                    "Exploring open source"
                            );

                            newUser.setQuote(
                                    "Open source today, a brighter tomorrow"
                            );

                            // Google verified the email
                            newUser.setEmailVerified(true);

                            return userRepository.save(newUser);
                        });

        /*
         * Existing local account.
         *
         * Google has verified this email.
         */
        if (!user.isEmailVerified()) {

            user.setEmailVerified(true);

            userRepository.save(user);
        }

        /*
         * IMPORTANT:
         *
         * Spring's OAuth2 authentication name may be
         * Google's subject ID.
         *
         * Our application uses EMAIL as the username.
         *
         * Therefore create a new Authentication whose
         * principal name is the user's email.
         */
        GoogleOAuth2Principal principal =
                new GoogleOAuth2Principal(
                        oauth2User,
                        normalizedEmail
                );

        Authentication emailAuthentication =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        authentication.getAuthorities()
                );

        /*
         * Put email-based authentication into SecurityContext.
         */
        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(
                emailAuthentication
        );

        SecurityContextHolder.setContext(context);

        /*
         * Save SecurityContext into HTTP session.
         */
        securityContextRepository.saveContext(
                context,
                request,
                response
        );

        /*
         * Redirect to Next.js.
         *
         * Next.js will call:
         *
         * GET /api/auth/me
         */
        response.sendRedirect(frontendUrl);
    }
}