package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.Auth.AuthUserResponse;
import com.organization.gsoc.DTO.Auth.LoginRequest;
import com.organization.gsoc.DTO.Auth.LoginResponse;
import com.organization.gsoc.DTO.Auth.SignupRequest;
import com.organization.gsoc.DTO.UpdateProfileRequest;
import com.organization.gsoc.Service.AuthService;
import com.organization.gsoc.Service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;
    private final UserProfileService userProfileService;

    public AuthControllerImpl(
            AuthService authService,
            UserProfileService userProfileService
    ) {
        this.authService = authService;
        this.userProfileService = userProfileService;
    }

    @Override
    public ResponseEntity<Map<String, String>> signup(
            SignupRequest request
    ) {

        authService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "message",
                        "Registration successful. Please verify your email."
                ));
    }

    @Override
    public ResponseEntity<Map<String, String>> verifyEmail(
            String token
    ) {

        authService.verifyEmail(token);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Email verified successfully"
                )
        );
    }

    @Override
    public ResponseEntity<AuthUserResponse> login(
            LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {

        LoginResponse user = authService.login(
                request,
                httpRequest,
                httpResponse
        );

        /*
         * Login only returns basic authenticated-user information.
         * The frontend can call GET /api/auth/me to get the
         * complete profile and bookmarks.
         */
        AuthUserResponse response =
                userProfileService.getCurrentUser(user.email());

        response.add(
                linkTo(methodOn(AuthController.class)
                        .getCurrentUser(
                                SecurityContextHolder.getContext()
                                        .getAuthentication()
                        ))
                        .withRel("me")
        );

        response.add(
                linkTo(methodOn(AuthController.class)
                        .logout(null, null))
                        .withRel("logout")
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AuthUserResponse> getCurrentUser(
            Authentication authentication
    ) {

        AuthUserResponse response =
                userProfileService.getCurrentUser(
                        authentication.getName()
                );

        response.add(
                linkTo(methodOn(AuthController.class)
                        .getCurrentUser(authentication))
                        .withSelfRel()
        );

        response.add(
                linkTo(methodOn(AuthController.class)
                        .getBookmarks(authentication))
                        .withRel("bookmarks")
        );

        response.add(
                linkTo(methodOn(AuthController.class)
                        .logout(null, null))
                        .withRel("logout")
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AuthUserResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request
    ) {

        AuthUserResponse response =
                userProfileService.updateProfile(
                        authentication.getName(),
                        request
                );

        response.add(
                linkTo(methodOn(AuthController.class)
                        .getCurrentUser(authentication))
                        .withSelfRel()
        );

        response.add(
                linkTo(methodOn(AuthController.class)
                        .getBookmarks(authentication))
                        .withRel("bookmarks")
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Map<String, String>> addBookmark(
            Authentication authentication,
            @PathVariable UUID organizationId
    ) {

        userProfileService.addBookmark(
                authentication.getName(),
                organizationId
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Organization bookmarked successfully"
                )
        );
    }

    @Override
    public ResponseEntity<Map<String, String>> removeBookmark(
            Authentication authentication,
            @PathVariable UUID organizationId
    ) {

        userProfileService.removeBookmark(
                authentication.getName(),
                organizationId
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Organization bookmark removed successfully"
                )
        );
    }

    @Override
    public ResponseEntity<AuthUserResponse> getBookmarks(
            Authentication authentication
    ) {

        AuthUserResponse response =
                userProfileService.getBookmarks(
                        authentication.getName()
                );

        response.add(
                linkTo(methodOn(AuthController.class)
                        .getBookmarks(authentication))
                        .withSelfRel()
        );

        response.add(
                linkTo(methodOn(AuthController.class)
                        .getCurrentUser(authentication))
                        .withRel("profile")
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Map<String, String>> logout(
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {

        authService.logout(
                httpRequest,
                httpResponse
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Logged out successfully"
                )
        );
    }
}