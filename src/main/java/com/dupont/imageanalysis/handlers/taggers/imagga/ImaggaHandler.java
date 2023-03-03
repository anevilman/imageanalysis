package com.dupont.imageanalysis.handlers.taggers.imagga;

import com.dupont.imageanalysis.handlers.taggers.ObjectTagger;
import com.dupont.imageanalysis.models.ImageSubmission;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Profile("imagga")
public class ImaggaHandler implements ObjectTagger {

    private static final String API_SECRET = "Basic YWNjXzc2MGNhOGExZDE3Mzc2YTo4NzE2N2NhOGY4ZjIwOWQxN2I5NDNiMjQzNmRmOWUyNA==";

    private final RestTemplate restTemplate;

    public ImaggaHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    private UriComponentsBuilder startURIBuild(String path) {
        return  UriComponentsBuilder
                .fromUriString("https://api.imagga.com")
                .pathSegment("v2", path);
    }

    private void updateHeadersWithAuth(HttpHeaders headers) {
        headers.setBasicAuth(API_SECRET);
    }

    private String uploadImage(byte[] imageFile) {
        URI uploadURI = startURIBuild("uploads").build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        updateHeadersWithAuth(headers);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("image_base64", Base64.getEncoder().encodeToString(imageFile));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<ImaggaUploadResponse> response = restTemplate.postForEntity(uploadURI, request, ImaggaUploadResponse.class);
        return Optional.ofNullable(response.getBody())
                .map(ImaggaUploadResponse::getResult)
                .map(ImaggaUploadResult::getUploadId)
                .orElseThrow(RuntimeException::new); //TODO throw better exception
    }

    @Override
    public Set<String> findObjects(ImageSubmission imageSubmission) {
        UriComponentsBuilder builder = startURIBuild("tags");
        if (imageSubmission.getImage() != null) {
            String uploadId = uploadImage(imageSubmission.getImage());
            builder.queryParam("image_upload_id", uploadId);
        } else {
            builder.queryParam("image_url", imageSubmission.getImageUrl());
        }

        ResponseEntity<ImaggaTagResponse> response = restTemplate.getForEntity(builder.build().toUri(), ImaggaTagResponse.class);

        return Optional.ofNullable(response.getBody())
                .map(ImaggaTagResponse::getResult)
                .map(ImaggaTagList::getTags)
                .stream()
                .flatMap(Collection::stream)
                .map(ImaggaTag::getTag)
                .map(ImaggaTagValue::getEn)
                .collect(Collectors.toSet());
    }

}
