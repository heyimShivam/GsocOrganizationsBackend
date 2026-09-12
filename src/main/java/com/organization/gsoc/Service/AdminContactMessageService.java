package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.AdminContactMessageResponse;
import com.organization.gsoc.DTO.ContactMessageReplyRequest;
import com.organization.gsoc.DTO.ContactMessageStatusRequest;

import java.util.List;
import java.util.UUID;

public interface AdminContactMessageService {

    List<AdminContactMessageResponse> getAllMessages();

    AdminContactMessageResponse getMessageById(UUID id);

    AdminContactMessageResponse updateStatus(
            UUID id,
            ContactMessageStatusRequest request
    );

    AdminContactMessageResponse replyToMessage(
            UUID id,
            ContactMessageReplyRequest request
    );
}