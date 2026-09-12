package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.AdminContactMessageResponse;
import com.organization.gsoc.DTO.ContactMessageReplyRequest;
import com.organization.gsoc.DTO.ContactMessageStatusRequest;
import com.organization.gsoc.Service.AdminContactMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class AdminContactMessageControllerImpl
        implements AdminContactMessageController {

    private final AdminContactMessageService adminContactMessageService;

    public AdminContactMessageControllerImpl(
            AdminContactMessageService adminContactMessageService
    ) {
        this.adminContactMessageService = adminContactMessageService;
    }

    @Override
    public ResponseEntity<List<AdminContactMessageResponse>> getAllMessages() {

        return ResponseEntity.ok(
                adminContactMessageService.getAllMessages()
        );
    }

    @Override
    public ResponseEntity<AdminContactMessageResponse> getMessageById(
            UUID id
    ) {

        return ResponseEntity.ok(
                adminContactMessageService.getMessageById(id)
        );
    }

    @Override
    public ResponseEntity<AdminContactMessageResponse> updateStatus(
            UUID id,
            ContactMessageStatusRequest request
    ) {

        return ResponseEntity.ok(
                adminContactMessageService.updateStatus(id, request)
        );
    }

    @Override
    public ResponseEntity<AdminContactMessageResponse> replyToMessage(
            UUID id,
            ContactMessageReplyRequest request
    ) {

        return ResponseEntity.ok(
                adminContactMessageService.replyToMessage(id, request)
        );
    }
}