package com.organization.gsoc.Service;

import org.springframework.security.core.context.SecurityContext;
import com.organization.gsoc.DTO.Auth.LoginRequest;
import com.organization.gsoc.DTO.Auth.LoginResponse;
import com.organization.gsoc.DTO.Auth.SignupRequest;
import com.organization.gsoc.Entity.EmailVerificationTokenEntity;
import com.organization.gsoc.Entity.UserEntity;
import com.organization.gsoc.Enums.UserRole;
import com.organization.gsoc.Exception.*;
import com.organization.gsoc.Repository.EmailVerificationTokenRepository;
import com.organization.gsoc.Repository.UserRepository;
import com.organization.gsoc.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationTokenRepository tokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailVerificationTokenRepository tokenRepository,
            EmailService emailService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository =
                new HttpSessionSecurityContextRepository();
    }

    private String generateVerificationToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        return java.util.Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    @Override
    @Transactional
    public void signup(SignupRequest request) {
        System.out.println(System.getenv("MAIL_USERNAME"));
        System.out.println(System.getenv("MAIL_PASSWORD"));
        // 1. Check password confirmation
        if (!request.password().equals(request.confirmPassword())) {
            throw new PasswordMismatchException(
                    "Password and confirm password do not match"
            );
        }

        // 2. Normalize email
        String email = request.email()
                .trim()
                .toLowerCase();

        // 3. Check whether email already exists
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(
                    "An account with this email already exists"
            );
        }

        // 4. Hash password using BCrypt
        String hashedPassword =
                passwordEncoder.encode(request.password());

        // 5. Create user
        UserEntity user = new UserEntity();

        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setGithubUsername(request.githubUsername());
        user.setRole(UserRole.USER);
        user.setDescription("Exploring open source");
        user.setQuote("Open source today, a brighter tomorrow");

        // 6. Save user first
        UserEntity savedUser = userRepository.save(user);

        // 7. Generate verification token
        String token = generateVerificationToken();

        // 8. Create verification token entity
        EmailVerificationTokenEntity verificationToken =
                new EmailVerificationTokenEntity();

        verificationToken.setUser(savedUser);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(
                Instant.now().plus(24, ChronoUnit.HOURS)
        );
        verificationToken.setUsed(false);

        // 9. Save verification token
        tokenRepository.save(verificationToken);

//        10. Send Email
        emailService.sendVerificationEmail(
                savedUser.getEmail(),
                savedUser.getName(),
                token
        );
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {

        EmailVerificationTokenEntity verificationToken =
                tokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new InvalidVerificationTokenException(
                                        "Invalid verification token"
                                )
                        );

        if (verificationToken.isUsed()) {
            throw new VerificationTokenAlreadyUsedException(
                    "Verification token has already been used"
            );
        }

        if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
            throw new VerificationTokenExpiredException(
                    "Verification token has expired"
            );
        }

        UserEntity user = verificationToken.getUser();

        user.setEmailVerified(true);
        verificationToken.setUsed(true);

        userRepository.save(user);
        tokenRepository.save(verificationToken);
    }

    @Override
    public LoginResponse login(
            LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {

        String email = request.email()
                .trim()
                .toLowerCase();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.password()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException(
                    "Please verify your email before logging in"
            );
        }

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(
                context,
                httpRequest,
                httpResponse
        );

        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    @Override
    public LoginResponse getCurrentUser(Authentication authentication) {

        String email = authentication.getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new AuthenticatedUserNotFoundException(
                                "Authenticated user not found"
                        )
                );

        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    @Override
    public void logout(
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        SecurityContextHolder.clearContext();

        var session = httpRequest.getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }
}