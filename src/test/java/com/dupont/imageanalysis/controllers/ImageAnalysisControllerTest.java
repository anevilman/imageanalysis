package com.dupont.imageanalysis.controllers;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class ImageAnalysisControllerTest {

    private ImageAnalysisController testObject;


    @BeforeEach
    void beforeAll() {
        testObject = new ImageAnalysisController(null, null);
    }

    @Test
    void getImagesShouldReturnNonNull() {
        Assertions.assertNotNull(testObject.getImages(Collections.emptySet()));
    }

}
