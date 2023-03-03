package com.dupont.imageanalysis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ImageSubmission {

    private String label;
    private byte[] image;
    private String imageUrl;
    private Boolean detectObjects;

}
