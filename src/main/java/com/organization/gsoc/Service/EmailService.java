package com.organization.gsoc.Service;

public interface EmailService {

    void sendVerificationEmail(
            String email,
            String name,
            String token
    );
}
