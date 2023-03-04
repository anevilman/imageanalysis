package com.dupont.imageanalysis.controllers;


import com.dupont.imageanalysis.handlers.ImageGetHandler;
import com.dupont.imageanalysis.handlers.ImagePostHandler;
import com.dupont.imageanalysis.models.ImageSubmission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class ImageAnalysisControllerTest {

    private ImageAnalysisController testObject;
    @Mock
    private ImageGetHandler imageGetHandler;
    @Mock
    private ImagePostHandler imagePostHandler;

    @BeforeEach
    void beforeAll() {
        testObject = new ImageAnalysisController(imageGetHandler, imagePostHandler);
    }

    @Test
    void getImagesShouldCallGetHandler() {
        testObject.getImages(Collections.emptySet());
        Mockito.verify(imageGetHandler).getImages(Collections.emptySet());
    }

    @Test
    void getImageShouldCallGetHandler() {
        testObject.getImage("text");
        Mockito.verify(imageGetHandler).getImage("text");
    }

    @Test
    void postImageShouldCallPostHandler() {
        testObject.submitImage(new ImageSubmission());
        Mockito.verify(imagePostHandler).submitImage(Mockito.any());
    }
}
