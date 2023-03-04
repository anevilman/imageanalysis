package com.dupont.imageanalysis.handlers.taggers.imagga;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImaggaUploadResponse {

    private ImaggaUploadResult result;
    private ImaggaStatus status;


}
