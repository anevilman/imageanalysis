package com.dupont.imageanalysis.handlers.taggers.imagga;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class ImaggaUploadResult {

    @JsonAlias("upload_id")
    private String uploadId;

}
