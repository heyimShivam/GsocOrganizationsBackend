package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.AdminContactMessageResponse;
import com.organization.gsoc.DTO.ContactMessageReplyRequest;
import com.organization.gsoc.DTO.ContactMessageStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/admin/contact-us")
public interface AdminContactMessageController {

    @GetMapping
    ResponseEntity<List<AdminContactMessageResponse>> getAllMessages();

    @GetMapping("/{id}")
    ResponseEntity<AdminContactMessageResponse> getMessageById(
            @PathVariable UUID id
    );

    @PatchMapping("/{id}/status")
    ResponseEntity<AdminContactMessageResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ContactMessageStatusRequest request
    );

    @PatchMapping("/{id}/reply")
    ResponseEntity<AdminContactMessageResponse> replyToMessage(
            @PathVariable UUID id,
            @Valid @RequestBody ContactMessageReplyRequest request
    );
}