package com.organization.gsoc.Config;

import com.organization.gsoc.Entity.UserEntity;
import com.organization.gsoc.Enums.UserRole;
import com.organization.gsoc.Repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;

@Component
public class GoogleOAuth2SuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

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

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

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

        String normalizedEmail =
                email.trim().toLowerCase();

        UserEntity user = userRepository
                .findByEmail(normalizedEmail)
                .orElseGet(() -> {

                    UserEntity newUser = new UserEntity();

                    newUser.setName(
                            name != null && !name.isBlank()
                                    ? name
                                    : normalizedEmail
                    );

                    newUser.setEmail(normalizedEmail);

                    // Google-only account
                    newUser.setPassword(null);

                    newUser.setRole(UserRole.USER);
                    newUser.setDescription("Exploring open source");
                    newUser.setQuote("Open source today, a brighter tomorrow");

                    // Google verified the email
                    newUser.setEmailVerified(true);

                    return userRepository.save(newUser);
                });

        // Existing account whose email was not verified locally
        if (!user.isEmailVerified()) {
            user.setEmailVerified(true);
            userRepository.save(user);
        }

        response.sendRedirect(frontendUrl);
    }
}