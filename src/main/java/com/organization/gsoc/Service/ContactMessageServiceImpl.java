package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.ContactMessageRequest;
import com.organization.gsoc.DTO.ContactMessageResponse;
import com.organization.gsoc.Entity.ContactMessageEntity;
import com.organization.gsoc.Repository.ContactMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageServiceImpl(
            ContactMessageRepository contactMessageRepository
    ) {
        this.contactMessageRepository = contactMessageRepository;
    }

    @Override
    public ContactMessageResponse createMessage(ContactMessageRequest request) {

        ContactMessageEntity entity = new ContactMessageEntity();

        entity.setName(request.name().trim());
        entity.setEmail(request.email().trim().toLowerCase());
        entity.setSubject(request.subject().trim());
        entity.setMessage(request.message().trim());

        ContactMessageEntity saved =
                contactMessageRepository.save(entity);

        return new ContactMessageResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getSubject(),
                saved.getMessage(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }
}