package com.dupont.imageanalysis.models;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Image {

    private String id;
    private String label;
    private Set<String> objects;

}
