package com.organization.gsoc.Exception;

public class VerificationTokenAlreadyUsedException extends RuntimeException {

    public VerificationTokenAlreadyUsedException(String message) {
        super(message);
    }
}