package com.organization.gsoc.Exception;

public class AuthenticatedUserNotFoundException
        extends RuntimeException {

    public AuthenticatedUserNotFoundException(String message) {
        super(message);
    }
}