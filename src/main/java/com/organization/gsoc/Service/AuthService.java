package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.Auth.LoginRequest;
import com.organization.gsoc.DTO.Auth.LoginResponse;
import com.organization.gsoc.DTO.Auth.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

    void signup(SignupRequest request);
    void verifyEmail(String token);
//    Important Saving user data cookies
    LoginResponse login(
            LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    );

//    who i am ?
    LoginResponse getCurrentUser(Authentication authentication);

//    logout
    void logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse);
}