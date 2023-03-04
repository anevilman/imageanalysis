package com.dupont.imageanalysis.handlers.taggers.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MockTaggerTest {

    @Test
    @DisplayName("should create two random tags")
    void shouldCreateTwoRandomTags() {
        Assertions.assertEquals(2, new MockTagger().findObjects(null).size());
    }

}
