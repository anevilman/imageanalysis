package com.dupont.imageanalysis.handlers;

import com.dupont.imageanalysis.db.ImageDBModel;
import com.dupont.imageanalysis.db.ImageRepository;
import com.dupont.imageanalysis.db.ObjectDBModel;
import com.dupont.imageanalysis.db.ObjectRepository;
import com.dupont.imageanalysis.exceptions.ImageNotFoundException;
import com.dupont.imageanalysis.models.Image;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ImageGetHandler {
    private final ImageRepository imageRepository;
    private final ObjectRepository objectRepository;

    private ImageGetHandler(ImageRepository imageRepository, ObjectRepository objectRepository) {
        this.imageRepository = imageRepository;
        this.objectRepository = objectRepository;
    }

    public List<Image> getImages(Set<String> objects) {
        return objects.isEmpty() ? getAllImages() : getObjectImages(objects);
    }

    private List<Image> getAllImages() {
        return StreamSupport.stream(imageRepository.findAll().spliterator(), false)
                .map(ImageDBModel::mapToOutputModel)
                .collect(Collectors.toList());
    }

    private List<Image> getObjectImages(Set<String> objects) {
        List<ObjectDBModel> dbObjects = objectRepository.findByObjectNameIn(objects);
        List<Image> result;
        if (dbObjects.isEmpty()) {
            result = Collections.emptyList();
        } else {
            result = imageRepository.findByImageObjectsIn(dbObjects).stream()
                    .distinct()
                    .map(ImageDBModel::mapToOutputModel)
                    .collect(Collectors.toList());
            if (result.isEmpty()) {
                throw new ImageNotFoundException(objects);
            }
        }
        return result;
    }

    public Image getImage(String imageId) {
        return imageRepository.findByImageId(imageId).map(ImageDBModel::mapToOutputModel).orElseThrow(() -> new ImageNotFoundException(imageId));
    }


}
