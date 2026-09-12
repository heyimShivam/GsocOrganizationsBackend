package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.ContactMessageRequest;
import com.organization.gsoc.DTO.ContactMessageResponse;
import com.organization.gsoc.Service.ContactMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactMessageControllerImpl
        implements ContactMessageController {

    private final ContactMessageService contactMessageService;

    public ContactMessageControllerImpl(
            ContactMessageService contactMessageService
    ) {
        this.contactMessageService = contactMessageService;
    }

    @Override
    public ResponseEntity<ContactMessageResponse> createMessage(
            ContactMessageRequest request
    ) {
        ContactMessageResponse response =
                contactMessageService.createMessage(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}