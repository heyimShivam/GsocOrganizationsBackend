package com.organization.gsoc.Exception;

public class GithubUsernameAlreadyExistsException
        extends RuntimeException {

    public GithubUsernameAlreadyExistsException(String message) {
        super(message);
    }
}