package com.dupont.imageanalysis.model;

import com.dupont.imageanalysis.exceptions.InvalidRequestException;
import com.dupont.imageanalysis.models.ImageSubmission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ImageSubmissionsTest {

    @Test
    @DisplayName("should throw exception if no image provided")
    void shouldThrowExceptionIfNoImageProvided() {
        Assertions.assertThrows(InvalidRequestException.class, () -> ImageSubmission.builder().build().validateSubmission());
    }

    @Test
    @DisplayName("should throw exception if both images provided")
    void shouldThrowExceptionIfBothImagesProvided() {
        Assertions.assertThrows(InvalidRequestException.class,
                () -> ImageSubmission.builder()
                        .image(new byte[0])
                        .imageUrl("")
                        .build()
                        .validateSubmission());
    }

    @Test
    @DisplayName("should throw exception if bad image URL")
    void shouldThrowExceptionIfBadImageURL() {
        Assertions.assertThrows(InvalidRequestException.class,
                () -> ImageSubmission.builder()
                        .imageUrl("not a url")
                        .build()
                        .validateSubmission());
    }

    @Test
    @DisplayName("should not throw exception if good URL")
    void shouldNotThrowExceptionIfGoodURL() {
        Assertions.assertDoesNotThrow(() -> ImageSubmission.builder()
                        .imageUrl("http://test.x")
                        .build()
                        .validateSubmission());
    }

}
