package com.dupont.imageanalysis.handlers.taggers.imagga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class ImaggaTag {

    private Double confidence;
    private ImaggaTagValue tag;
}
