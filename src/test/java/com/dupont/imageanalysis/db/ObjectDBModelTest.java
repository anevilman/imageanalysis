package com.dupont.imageanalysis.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ObjectDBModelTest {

    @Test
    @DisplayName("should return empty list for null collection")
    void shouldReturnEmptyListForNullCollection() {
        Assertions.assertEquals(0, ObjectDBModel.convertCollection(null).size());
    }

    @Test
    @DisplayName("should return empty list for empty collection")
    void shouldReturnEmptyListForEmptyCollection() {
        Assertions.assertEquals(0, ObjectDBModel.convertCollection(Collections.emptySet()).size());
    }

    @Test
    @DisplayName("should convert collection")
    void shouldConvertCollection() {
        List<ObjectDBModel> objects = List.of(
                ObjectDBModel.builder()
                        .objectName("NAME1")
                        .objectsPK(1L)
                        .build(),
                ObjectDBModel.builder()
                        .objectName("NAME2")
                        .objectsPK(2L)
                        .build()
        );
        Assertions.assertEquals(Set.of("NAME1", "NAME2"), ObjectDBModel.convertCollection(objects));
    }

}
