package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.Auth.AuthUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/api")
public interface UserProfile {
    @GetMapping("/user-profile/{githubId}")
     public ResponseEntity<AuthUserResponse> getUserProfileByGithubId(
            @PathVariable String githubId
    );
}
