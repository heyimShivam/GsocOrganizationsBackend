package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.Auth.AuthUserResponse;
import com.organization.gsoc.Service.AuthService;
import com.organization.gsoc.Service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class UserProfileImpl implements  UserProfile {
    private final UserProfileService userProfileService;

    public UserProfileImpl(
            UserProfileService userProfileService
    ) {
        this.userProfileService = userProfileService;
    }

    @Override
    public ResponseEntity<AuthUserResponse> getUserProfileByGithubId(
            @PathVariable String githubId
    ) {
        AuthUserResponse response = userProfileService.getCurrentUserByGithub(githubId);

        return ResponseEntity.ok(response);
    }
}
