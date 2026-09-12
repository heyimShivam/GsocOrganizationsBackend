package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.Auth.AuthUserResponse;
import com.organization.gsoc.DTO.Auth.LoginRequest;
import com.organization.gsoc.DTO.Auth.LoginResponse;
import com.organization.gsoc.DTO.Auth.SignupRequest;
import com.organization.gsoc.DTO.UpdateProfileRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/auth")
public interface AuthController {

    @PostMapping("/signup")
    ResponseEntity<Map<String, String>> signup(
            @Valid @RequestBody SignupRequest request
    );

    @GetMapping("/verify-email")
    ResponseEntity<Map<String, String>> verifyEmail(
            @RequestParam String token
    );

    @PostMapping("/login")
    ResponseEntity<AuthUserResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    );

    @GetMapping("/me")
    ResponseEntity<AuthUserResponse> getCurrentUser(
            Authentication authentication
    );

    @PostMapping("/logout")
    ResponseEntity<Map<String, String>> logout(
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    );

    @PatchMapping("/me")
    ResponseEntity<AuthUserResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request
    );

    @GetMapping("/me/bookmarks")
    ResponseEntity<AuthUserResponse> getBookmarks(
            Authentication authentication
    );

    @PostMapping("/me/bookmarks/{organizationId}")
    ResponseEntity<Map<String, String>> addBookmark(
            Authentication authentication,
            @PathVariable UUID organizationId
    );

    @DeleteMapping("/me/bookmarks/{organizationId}")
    ResponseEntity<Map<String, String>> removeBookmark(
            Authentication authentication,
            @PathVariable UUID organizationId
    );
}