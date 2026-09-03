package com.organization.gsoc.Exception;

public class OrganicationNotFoundException extends RuntimeException {
    public OrganicationNotFoundException(String message) {
        super(message);
    }
}
