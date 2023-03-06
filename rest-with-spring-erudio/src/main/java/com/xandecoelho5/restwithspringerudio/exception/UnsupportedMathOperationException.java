package com.xandecoelho5.restwithspringerudio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedMathOperationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnsupportedMathOperationException(String exception) {
        super(exception);
    }
}
