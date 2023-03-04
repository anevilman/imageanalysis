package com.dupont.imageanalysis.models;

import com.dupont.imageanalysis.exceptions.InvalidRequestException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ImageSubmission {

    private String label;
    private byte[] image;
    private String imageUrl;
    private Boolean detectObjects;

    public void validateSubmission() {
        Optional<byte[]> imageOpt = Optional.ofNullable(image);
        Optional<String> imageUrlOpt = Optional.ofNullable(imageUrl);

        if (imageOpt.isEmpty() && imageUrlOpt.isEmpty()) {
            throw new InvalidRequestException("Must supply one of image or imageUrl");
        }

        if (imageOpt.isPresent() && imageUrlOpt.isPresent()) {
            throw new InvalidRequestException("Must supply either image or imageUrl, not both");
        }

        imageUrlOpt
                .ifPresent(url -> {
                    try {
                        new URL(url);
                    } catch (MalformedURLException exc) {
                        throw new InvalidRequestException("Must provide a valid URL for imageUrl");
                    }
                });

    }

}
