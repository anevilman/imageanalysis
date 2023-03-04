package com.dupont.imageanalysis.handlers;

import com.dupont.imageanalysis.db.ImageDBModel;
import com.dupont.imageanalysis.db.ImageRepository;
import com.dupont.imageanalysis.db.ObjectRepository;
import com.dupont.imageanalysis.exceptions.ImageNotFoundException;
import com.dupont.imageanalysis.models.Image;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class ImageGetHandlerTest {

    private static final List<ImageDBModel> MODELS = List.of(
            ImageDBModel.builder()
                    .imageLabel("X")
                    .build(),
            ImageDBModel.builder()
                    .imageLabel("Z")
                    .build()
    );

    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ObjectRepository objectRepository;

    private ImageGetHandler imageGetHandler;

    @BeforeEach
    void initializeTest() {
        imageGetHandler = new ImageGetHandler(imageRepository, objectRepository);
    }

    @Test
    @DisplayName("should throw exception if no image found")
    void shouldThrowExceptionIfNoImageFound() {
        Mockito.when(imageRepository.findByImageId("X")).thenReturn(Optional.empty());

        Assertions.assertThrows(ImageNotFoundException.class, () -> imageGetHandler.getImage("X"));
    }

    @Test
    @DisplayName("should return mapped object")
    void shouldReturnMappedObject() {
        Mockito.when(imageRepository.findByImageId("X")).thenReturn(Optional.of(ImageDBModel.builder().imageLabel("Z").build()));

        Assertions.assertEquals("Z", imageGetHandler.getImage("X").getLabel());
    }

    @Test
    @DisplayName("should throw exception if no objects found for query")
    void shouldThrowExceptionIfNoImagesFoundForObjects() {
        Mockito.when(objectRepository.findByObjectNameIn(Mockito.any())).thenReturn(Collections.emptyList());
        Mockito.when(imageRepository.findByImageObjectsIn(Mockito.any())).thenReturn(Collections.emptyList());

        Assertions.assertThrows(ImageNotFoundException.class, () -> imageGetHandler.getObjectImages(Collections.emptySet()));
    }

    @Test
    @DisplayName("should return converted lists")
    void shouldReturnConvertedLists() {
        Mockito.when(objectRepository.findByObjectNameIn(Mockito.any())).thenReturn(Collections.emptyList());
        Mockito.when(imageRepository.findByImageObjectsIn(Mockito.any())).thenReturn(MODELS);

        List<Image> images = imageGetHandler.getObjectImages(Collections.emptySet());

        Assertions.assertEquals("XZ", images.stream().map(Image::getLabel).sorted().collect(Collectors.joining()));
    }

    @Test
    @DisplayName("should return all images mapped")
    void shouldReturnAllImagesMapped() {
        Mockito.when(imageRepository.findAll()).thenReturn(MODELS);

        List<Image> images = imageGetHandler.getAllImages();

        Assertions.assertEquals("XZ", images.stream().map(Image::getLabel).sorted().collect(Collectors.joining()));
    }

    @Test
    @DisplayName("empty objects list should use find all")
    void emptyObjectsListShouldUseFindAll() {
        Mockito.when(imageRepository.findAll()).thenReturn(MODELS);

        imageGetHandler.getImages(Collections.emptySet());

        Mockito.verify(imageRepository).findAll();
    }

    @Test
    @DisplayName("non-empty object list should use find by objects")
    void nonEmptyObjectListShouldUseFindByObjects() {
        Mockito.when(objectRepository.findByObjectNameIn(Mockito.any())).thenReturn(Collections.emptyList());
        Mockito.when(imageRepository.findByImageObjectsIn(Mockito.any())).thenReturn(MODELS);

        imageGetHandler.getImages(Set.of("X"));

        Mockito.verify(imageRepository).findByImageObjectsIn(Mockito.any());
    }

}
