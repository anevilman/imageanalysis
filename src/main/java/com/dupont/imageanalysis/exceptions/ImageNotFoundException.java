package com.dupont.imageanalysis.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

public class ImageNotFoundException extends ResponseStatusException {

    public ImageNotFoundException(String imageId) {
        super(HttpStatus.NOT_FOUND, String.format("No image found for image ID: %s", imageId));
    }

    public ImageNotFoundException(Set<String> objects) {
        super(HttpStatus.NOT_FOUND, String.format("No images found for provided objects: %s", objects.toString()));
    }

}
