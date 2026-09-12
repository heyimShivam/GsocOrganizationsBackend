package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.Auth.AuthUserResponse;
import com.organization.gsoc.DTO.UpdateProfileRequest;
import com.organization.gsoc.Entity.UserBookmarkEntity;
import com.organization.gsoc.Entity.UserEntity;
import com.organization.gsoc.Exception.AuthenticatedUserNotFoundException;
import com.organization.gsoc.Exception.OrganizationNotFoundException;
import com.organization.gsoc.Repository.OrganizationRepository;
import com.organization.gsoc.Repository.UserBookmarkRepository;
import com.organization.gsoc.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final UserBookmarkRepository userBookmarkRepository;

    public UserProfileServiceImpl(
            UserRepository userRepository,
            UserBookmarkRepository userBookmarkRepository,
            OrganizationRepository organizationRepository
    ) {
        this.userRepository = userRepository;
        this.userBookmarkRepository = userBookmarkRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthUserResponse getCurrentUser(String email) {

        UserEntity user = getUserByEmail(email);

        return buildUserResponse(user);
    }

    @Override
    public AuthUserResponse updateProfile(
            String email,
            UpdateProfileRequest request
    ) {

        UserEntity user = getUserByEmail(email);

        user.setName(request.name().trim());
        user.setDescription(request.description());
        user.setQuote(request.quote());
        user.setGithubUsername(request.githubUsername());

        userRepository.save(user);

        return buildUserResponse(user);
    }

    @Override
    public void addBookmark(
            String email,
            UUID organizationId
    ) {

        UserEntity user = getUserByEmail(email);

        if (!organizationRepository.existsById(organizationId)) {
            throw new OrganizationNotFoundException(
                    "Organization not found: " + organizationId
            );
        }

        if (userBookmarkRepository
                .existsByUserIdAndOrganizationId(
                        user.getId(),
                        organizationId
                )) {
            return;
        }

        UserBookmarkEntity bookmark = new UserBookmarkEntity();

        bookmark.setUser(user);
        bookmark.setOrganizationId(organizationId);

        userBookmarkRepository.save(bookmark);
    }

    @Override
    public void removeBookmark(
            String email,
            UUID organizationId
    ) {

        UserEntity user = getUserByEmail(email);

        userBookmarkRepository.deleteByUserIdAndOrganizationId(
                user.getId(),
                organizationId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AuthUserResponse getBookmarks(String email) {

        UserEntity user = getUserByEmail(email);

        return buildUserResponse(user);
    }

    private UserEntity getUserByEmail(String email) {

        return userRepository.findByEmail(
                email.trim().toLowerCase()
        ).orElseThrow(() ->
                new AuthenticatedUserNotFoundException(
                        "Authenticated user not found"
                )
        );
    }

    private AuthUserResponse buildUserResponse(UserEntity user) {

        List<UUID> bookmarkedOrganizationIds =
                userBookmarkRepository
                        .findByUserId(user.getId())
                        .stream()
                        .map(UserBookmarkEntity::getOrganizationId)
                        .toList();

        return new AuthUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getDescription(),
                user.getQuote(),
                user.getGithubUsername(),
                bookmarkedOrganizationIds
        );
    }
}