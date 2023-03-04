package com.dupont.imageanalysis.db;

import com.dupont.imageanalysis.models.Image;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class ImageDBModelTest {

    @Test
    @DisplayName("should convert object correctly")
    void shouldConvertObjectCorrectly() {
        ImageDBModel model = ImageDBModel.builder()
                .imageId("ID")
                .imageLabel("LABEL")
                .imagesPK(1L)
                .imageObjects(
                        List.of(ObjectDBModel.builder().objectName("X").build())
                )
                .build();

        Image converted = ImageDBModel.mapToOutputModel(model);
        Assertions.assertEquals("ID", converted.getId());
        Assertions.assertEquals("LABEL", converted.getLabel());
        Assertions.assertEquals(Set.of("X"), converted.getObjects());
    }
}
