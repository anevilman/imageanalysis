package com.dupont.imageanalysis.controllers;

import com.dupont.imageanalysis.db.ImageDBModel;
import com.dupont.imageanalysis.db.ImageRepository;
import com.dupont.imageanalysis.db.ObjectDBModel;
import com.dupont.imageanalysis.db.ObjectRepository;
import com.dupont.imageanalysis.handlers.ImageGetHandler;
import com.dupont.imageanalysis.handlers.ImagePostHandler;
import com.dupont.imageanalysis.models.Image;
import com.dupont.imageanalysis.models.ImageSubmission;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("images")
public class ImageAnalysisController {

    private final ImageGetHandler imageGetHandler;
    private final ImagePostHandler imagePostHandler;

    public ImageAnalysisController(ImageGetHandler imageGetHandler, ImagePostHandler imagePostHandler) {
        this.imageGetHandler = imageGetHandler;
        this.imagePostHandler = imagePostHandler;
    }

    @GetMapping(produces = "application/json")
    public List<Image> getImages(@RequestParam(name = "objects", defaultValue = "") Set<String> objects) {
        return imageGetHandler.getImages(objects);
    }

    @GetMapping(value = "/{imageId}", produces = "application/json")
    public Image getImage(@PathVariable("imageId") String imageId) {
        return imageGetHandler.getImage(imageId);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Image submitImage(@RequestBody ImageSubmission imageSubmission) {
        imageSubmission.validateSubmission();
        return imagePostHandler.submitImage(imageSubmission);
    }

    @GetMapping(value = "/test")
    public String test() throws Exception {
        ImageSubmission submission = ImageSubmission.builder().image("x".getBytes(StandardCharsets.UTF_8)).build();
        System.out.println(new ObjectMapper().writeValueAsString(submission));
        return "TEST";
    }

}
