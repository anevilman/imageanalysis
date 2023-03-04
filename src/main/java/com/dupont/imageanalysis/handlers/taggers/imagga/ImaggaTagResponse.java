package com.dupont.imageanalysis.handlers.taggers.imagga;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class ImaggaTagResponse {

    private ImaggaTagList result;

}
