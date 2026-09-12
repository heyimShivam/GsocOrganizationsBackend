package com.organization.gsoc.Service.Impl;

import com.organization.gsoc.Service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(
            String email,
            String name,
            String token
    ) {

        String verificationLink =
                "http://localhost:3000/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Verify your GSoC Explorer account");

        message.setText(
                "Hello " + name + ",\n\n"
                        + "Thank you for signing up.\n\n"
                        + "Please verify your email address by clicking the link below:\n\n"
                        + verificationLink + "\n\n"
                        + "This verification link will expire soon.\n\n"
                        + "If you did not create this account, you can ignore this email.\n\n"
                        + "Regards,\n"
                        + "GSoC Explorer"
        );

        mailSender.send(message);
    }
}