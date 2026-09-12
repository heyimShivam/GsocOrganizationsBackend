package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.Auth.AuthUserResponse;
import com.organization.gsoc.DTO.UpdateProfileRequest;

import java.util.UUID;

public interface UserProfileService {

    AuthUserResponse getCurrentUser(String email);

    AuthUserResponse updateProfile(
            String email,
            UpdateProfileRequest request
    );

    void addBookmark(
            String email,
            UUID organizationId
    );

    void removeBookmark(
            String email,
            UUID organizationId
    );

    AuthUserResponse getBookmarks(String email);
}