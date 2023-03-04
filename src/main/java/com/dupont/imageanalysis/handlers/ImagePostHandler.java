package com.dupont.imageanalysis.handlers;

import com.dupont.imageanalysis.db.ImageDBModel;
import com.dupont.imageanalysis.db.ImageRepository;
import com.dupont.imageanalysis.db.ObjectDBModel;
import com.dupont.imageanalysis.db.ObjectRepository;
import com.dupont.imageanalysis.handlers.taggers.ObjectTagger;
import com.dupont.imageanalysis.models.Image;
import com.dupont.imageanalysis.models.ImageSubmission;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ImagePostHandler {

    private final ImageRepository imageRepository;
    private final ObjectRepository objectRepository;
    private final ObjectTagger objectTagger;

    public ImagePostHandler(ImageRepository imageRepository, ObjectRepository objectRepository, ObjectTagger objectTagger) {
        this.imageRepository = imageRepository;
        this.objectRepository = objectRepository;
        this.objectTagger = objectTagger;
    }

    public List<ObjectDBModel> selectAndInsertObjects(Set<String> objects) {
        List<ObjectDBModel> storedObjects = new LinkedList<>(objectRepository.findByObjectNameIn(objects));
        Set<String> storedObjectNames = storedObjects.stream().map(ObjectDBModel::getObjectName).collect(Collectors.toSet());
        objects.stream()
                .filter(object -> !storedObjectNames.contains(object))
                .map(object -> ObjectDBModel.builder().objectName(object).build())
                .map(objectRepository::save)
                .forEach(storedObjects::add);
        return storedObjects;
    }

    public Image submitImage(ImageSubmission imageSubmission) {
        String newId = UUID.randomUUID().toString();
        String newLabel = Optional.ofNullable(imageSubmission.getLabel()).orElse(newId);

        List<ObjectDBModel> objects =
                Optional.ofNullable(imageSubmission.getDetectObjects()).orElse(false)
                ? selectAndInsertObjects(objectTagger.findObjects(imageSubmission))
                : Collections.emptyList();

        ImageDBModel newImage = ImageDBModel.builder()
                .imageId(newId)
                .imageLabel(newLabel)
                .imageObjects(objects)
                .build();

        return ImageDBModel.mapToOutputModel(imageRepository.save(newImage));
    }

}
