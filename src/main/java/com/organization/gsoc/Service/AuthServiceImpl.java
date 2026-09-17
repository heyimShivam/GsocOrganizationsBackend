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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationTokenRepository tokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private static final Logger log =
            LoggerFactory.getLogger(AuthServiceImpl.class);
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
        log.info("Signup started for email={}", email);

        // 3. Check whether email already exists
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(
                    "An account with this email already exists"
            );
        }

        // 4. Normalize GitHub username
        String githubUsername = request.githubUsername();

        if (githubUsername != null) {
            githubUsername = githubUsername.trim();

            if (githubUsername.isBlank()) {
                githubUsername = null;
            }
        }

        // 5. Check GitHub username uniqueness
        if (githubUsername != null
                && userRepository.existsByGithubUsername(githubUsername)) {

            throw new GithubUsernameAlreadyExistsException(
                    "GitHub username is already associated with another account"
            );
        }

        // 6. Hash password
        String hashedPassword =
                passwordEncoder.encode(request.password());

        // 7. Create user
        UserEntity user = new UserEntity();

        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPassword(hashedPassword);

        // IMPORTANT: use normalized value
        user.setGithubUsername(githubUsername);

        user.setRole(UserRole.USER);
        user.setDescription("Exploring open source");
        user.setQuote("Open source today, a brighter tomorrow");

        // 8. Save user
        UserEntity savedUser = userRepository.save(user);
        log.info("User saved: id={}, email={}", savedUser.getId(), savedUser.getEmail());

        // 9. Generate verification token
        String token = generateVerificationToken();

        // 10. Create verification token
        EmailVerificationTokenEntity verificationToken =
                new EmailVerificationTokenEntity();

        verificationToken.setUser(savedUser);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(
                Instant.now().plus(24, ChronoUnit.HOURS)
        );
        verificationToken.setUsed(false);

        // 11. Save verification token
        tokenRepository.save(verificationToken);
        log.info("Verification token saved for userId={}", savedUser.getId());

        // 12. Send verification email
        try {
            log.info("Sending verification email to={}", savedUser.getEmail());

            emailService.sendVerificationEmail(
                    savedUser.getEmail(),
                    savedUser.getName(),
                    token
            );

            log.info("Verification email sent to={}", savedUser.getEmail());
        } catch (Exception ex) {
            log.error("Verification email sending failed for email={}",
                    savedUser.getEmail(), ex);

            throw new GithubUsernameAlreadyExistsException(
                    "Not able to send email"
            );
        }
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