package com.xandecoelho5.restwithspringerudio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtAuthenticationException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidJwtAuthenticationException(String exception) {
        super(exception);
    }
}