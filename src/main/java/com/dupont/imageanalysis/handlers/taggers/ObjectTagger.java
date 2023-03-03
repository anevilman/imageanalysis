package com.dupont.imageanalysis.handlers.taggers;

import com.dupont.imageanalysis.models.ImageSubmission;

import java.util.Set;

public interface ObjectTagger {

    Set<String> findObjects(ImageSubmission imageSubmission);
}
