package com.dupont.imageanalysis.handlers;

import com.dupont.imageanalysis.db.ImageRepository;
import com.dupont.imageanalysis.db.ObjectDBModel;
import com.dupont.imageanalysis.db.ObjectRepository;
import com.dupont.imageanalysis.handlers.taggers.ObjectTagger;
import com.dupont.imageanalysis.models.Image;
import com.dupont.imageanalysis.models.ImageSubmission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class ImagePostHandlerTest {

    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ObjectRepository objectRepository;
    @Mock
    private ObjectTagger objectTagger;

    private ImagePostHandler imagePostHandler;

    @BeforeEach
    void setup() {
        Mockito.lenient().when(imageRepository.save(Mockito.any())).thenAnswer(func -> func.getArguments()[0]);
        imagePostHandler = new ImagePostHandler(imageRepository, objectRepository, objectTagger);
    }

    @Test
    @DisplayName("test select and insert")
    void testSelectAndInsert() {
        Mockito.when(objectRepository.findByObjectNameIn(Mockito.any())).thenReturn(
                List.of(
                        ObjectDBModel.builder()
                                .objectName("X")
                                .build()
                )
        );

        List<ObjectDBModel> result = imagePostHandler.selectAndInsertObjects(Set.of("X", "Z"));

        Assertions.assertEquals(2, result.size());
        Mockito.verify(objectRepository, Mockito.times(1)).save(ObjectDBModel.builder().objectName("Z").build());
    }

    @Test
    @DisplayName("should use provided label")
    void shouldUseProvidedLabel() {
        ImageSubmission submission = ImageSubmission.builder().label("LABEL").build();

        Assertions.assertEquals("LABEL", imagePostHandler.submitImage(submission).getLabel());
    }

    @Test
    @DisplayName("should generate label that matches id if no label provided")
    void shouldGenerateLabelThatMatchesIdIfNoLabelProvided() {
        ImageSubmission submission = ImageSubmission.builder().build();

        Image result = imagePostHandler.submitImage(submission);

        Assertions.assertNotNull(result.getId());
        Assertions.assertNotNull(result.getLabel());
        Assertions.assertEquals(result.getId(), result.getLabel());
    }

    @ParameterizedTest
    @DisplayName("should not call object tagger if detect objects null or false")
    @NullSource
    @ValueSource(booleans = {false})
    void shouldNotCallObjectTaggerIfDetectObjectsNull(Boolean detectObjects) {
        ImageSubmission submission = ImageSubmission.builder().detectObjects(detectObjects).build();

        imagePostHandler.submitImage(submission);

        Mockito.verify(objectTagger, Mockito.times(0)).findObjects(Mockito.any());
    }

    @Test
    @DisplayName("should call object tagger if detect objects true")
    void shouldCallObjectTaggerIfDetectObjectsTrue() {
        Mockito.when(objectTagger.findObjects(Mockito.any())).thenReturn(Collections.emptySet());
        ImageSubmission submission = ImageSubmission.builder().detectObjects(true).build();

        imagePostHandler.submitImage(submission);

        Mockito.verify(objectTagger, Mockito.times(1)).findObjects(Mockito.any());
    }

    @Test
    @DisplayName("should save new image on submit")
    void shouldSaveNewImageOnSubmit() {
        ImageSubmission submission = ImageSubmission.builder().build();

        imagePostHandler.submitImage(submission);

        Mockito.verify(imageRepository, Mockito.times(1)).save(Mockito.any());
    }

}
