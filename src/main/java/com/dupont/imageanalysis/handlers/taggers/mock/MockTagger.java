package com.dupont.imageanalysis.handlers.taggers.mock;

import com.dupont.imageanalysis.handlers.taggers.ObjectTagger;
import com.dupont.imageanalysis.models.ImageSubmission;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Profile("mock")
public class MockTagger implements ObjectTagger {


    @Override
    public Set<String> findObjects(ImageSubmission imageSubmission) {
        return Stream.of(UUID.randomUUID().toString(), UUID.randomUUID().toString()).collect(Collectors.toSet());
    }
}
