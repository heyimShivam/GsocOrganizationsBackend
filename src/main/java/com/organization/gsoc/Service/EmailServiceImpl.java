package com.organization.gsoc.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private final RestClient resendClient;
    private final String frontendUrl;
    private final String fromEmail;

    public EmailServiceImpl(
            @Value("${resend.api-key}") String resendApiKey,
            @Value("${app.frontend.url}") String frontendUrl,
            @Value("${resend.from-email}") String fromEmail
    ) {
        this.frontendUrl = frontendUrl;
        this.fromEmail = fromEmail;

        this.resendClient = RestClient.builder()
                .baseUrl("https://api.resend.com")
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + resendApiKey
                )
                .build();
    }

    @Override
    public void sendVerificationEmail(
            String email,
            String name,
            String token
    ) {
        String verificationLink =
                frontendUrl + "/verify-email?token=" + token;

        String text =
                "Hello " + name + ",\n\n"
                        + "Thank you for signing up.\n\n"
                        + "Verify your email address:\n\n"
                        + verificationLink + "\n\n"
                        + "This verification link expires in 24 hours.\n\n"
                        + "Regards,\nGSoC Explorer";

        resendClient.post()
                .uri("/emails")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "from", fromEmail,
                        "to", List.of(email),
                        "subject", "Verify your GSoC Explorer account",
                        "text", text
                ))
                .retrieve()
                .toBodilessEntity();
    }
}