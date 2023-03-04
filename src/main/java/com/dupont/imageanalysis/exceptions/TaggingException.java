package com.dupont.imageanalysis.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TaggingException extends ResponseStatusException {

    public TaggingException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
