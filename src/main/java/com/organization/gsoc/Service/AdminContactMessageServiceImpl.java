package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.AdminContactMessageResponse;
import com.organization.gsoc.DTO.ContactMessageReplyRequest;
import com.organization.gsoc.DTO.ContactMessageStatusRequest;
import com.organization.gsoc.Entity.ContactMessageEntity;
import com.organization.gsoc.Repository.ContactMessageRepository;
import com.organization.gsoc.Service.AdminContactMessageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AdminContactMessageServiceImpl
        implements AdminContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    public AdminContactMessageServiceImpl(
            ContactMessageRepository contactMessageRepository
    ) {
        this.contactMessageRepository = contactMessageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminContactMessageResponse> getAllMessages() {

        return contactMessageRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminContactMessageResponse getMessageById(UUID id) {

        ContactMessageEntity message =
                contactMessageRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Contact message not found: " + id
                                )
                        );

        return toResponse(message);
    }

    @Override
    public AdminContactMessageResponse updateStatus(
            UUID id,
            ContactMessageStatusRequest request
    ) {

        ContactMessageEntity message =
                contactMessageRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Contact message not found: " + id
                                )
                        );

        message.setStatus(request.status());

        ContactMessageEntity saved =
                contactMessageRepository.save(message);

        return toResponse(saved);
    }

    @Override
    public AdminContactMessageResponse replyToMessage(
            UUID id,
            ContactMessageReplyRequest request
    ) {

        ContactMessageEntity message =
                contactMessageRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Contact message not found: " + id
                                )
                        );

        message.setAdminReply(request.adminReply().trim());
        message.setRepliedAt(Instant.now());

        /*
         * Once an admin replies, automatically move
         * the message to IN_PROGRESS unless it is already RESOLVED.
         */
        if (message.getStatus() !=
                com.organization.gsoc.Enums.ContactMessageStatus.RESOLVED) {

            message.setStatus(
                    com.organization.gsoc.Enums.ContactMessageStatus.IN_PROGRESS
            );
        }

        ContactMessageEntity saved =
                contactMessageRepository.save(message);

        return toResponse(saved);
    }

    private AdminContactMessageResponse toResponse(
            ContactMessageEntity message
    ) {

        return new AdminContactMessageResponse(
                message.getId(),
                message.getName(),
                message.getEmail(),
                message.getSubject(),
                message.getMessage(),
                message.getStatus(),
                message.getAdminReply(),
                message.getRepliedAt(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}