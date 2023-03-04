package com.dupont.imageanalysis.handlers.taggers.imagga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class ImaggaTagList {

    private List<ImaggaTag> tags;

}
