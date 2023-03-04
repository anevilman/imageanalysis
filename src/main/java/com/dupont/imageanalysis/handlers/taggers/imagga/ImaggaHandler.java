package com.dupont.imageanalysis.handlers.taggers.imagga;

import com.dupont.imageanalysis.exceptions.InvalidRequestException;
import com.dupont.imageanalysis.exceptions.TaggingException;
import com.dupont.imageanalysis.handlers.taggers.ObjectTagger;
import com.dupont.imageanalysis.models.ImageSubmission;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Profile("imagga")
public class ImaggaHandler implements ObjectTagger {

    private static final String API_USER = "acc_760ca8a1d17376a";
    private static final String API_SECRET = "87167ca8f8f209d17b943b2436df9e24";

    private final RestTemplate restTemplate;

    public ImaggaHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    static UriComponentsBuilder startURIBuild(String path) {
        return  UriComponentsBuilder
                .fromUriString("https://api.imagga.com")
                .pathSegment("v2", path);
    }

    static void updateHeadersWithAuth(HttpHeaders headers) {
        headers.setBasicAuth(API_USER, API_SECRET);
    }

    String uploadImage(byte[] imageFile) {
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
                .orElseThrow(() -> new TaggingException("Image upload failed"));
    }

    URI handleUpload(UriComponentsBuilder builder, byte[] image) {
        String uploadId = uploadImage(image);
        builder.queryParam("image_upload_id", uploadId);
        return builder.build().toUri();
    }

    //Putting sneaky throws here because there shouldn't be a way
    //to get a malformed URI
    @SneakyThrows
    URI handleURL(UriComponentsBuilder builder, String url) {
        //Don't like doing it this way, but couldn't find a way how to get
        //UriComponentsBuilder to correctly encode the image URL
        //It either wouldn't encode enough or would double-encode things
        String urlBase = builder.toUriString() + "?image_url=" + URLEncoder.encode(url, Charset.defaultCharset());
        return new URI(urlBase);
    }

    @Override
    public Set<String> findObjects(ImageSubmission imageSubmission) {
        UriComponentsBuilder builder = startURIBuild("tags");
        final URI imageUri = Optional.ofNullable(imageSubmission.getImage())
                .map(image -> handleUpload(builder, image))
                .orElseGet(() -> handleURL(builder, imageSubmission.getImageUrl()));

        HttpHeaders headers = new HttpHeaders();
        updateHeadersWithAuth(headers);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<ImaggaTagResponse> response = restTemplate.exchange(imageUri, HttpMethod.GET, request, ImaggaTagResponse.class);

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
