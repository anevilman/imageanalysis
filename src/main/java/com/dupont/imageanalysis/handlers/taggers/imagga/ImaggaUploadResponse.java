package com.dupont.imageanalysis.handlers.taggers.imagga;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImaggaUploadResponse {

    private ImaggaUploadResult result;
    private ImaggaStatus status;


}
