package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.ContactMessageRequest;
import com.organization.gsoc.DTO.ContactMessageResponse;

public interface ContactMessageService {

    ContactMessageResponse createMessage(ContactMessageRequest request);
}