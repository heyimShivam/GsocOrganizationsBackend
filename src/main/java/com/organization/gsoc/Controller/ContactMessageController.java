package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.ContactMessageRequest;
import com.organization.gsoc.DTO.ContactMessageResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
public interface ContactMessageController {

    @PostMapping("/contact-us")
    ResponseEntity<ContactMessageResponse> createMessage(
            @Valid @RequestBody ContactMessageRequest request
    );
}