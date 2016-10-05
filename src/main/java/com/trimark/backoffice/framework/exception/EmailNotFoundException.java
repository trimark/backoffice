package com.trimark.backoffice.framework.exception;


import org.springframework.security.core.AuthenticationException;

public class EmailNotFoundException extends AuthenticationException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
