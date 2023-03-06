package com.dupont.imageanalysis.handlers.taggers.imagga;

import com.dupont.imageanalysis.exceptions.TaggingException;
import com.dupont.imageanalysis.models.ImageSubmission;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class ImaggaHandlerTest {

    @Mock
    private RestTemplate restTemplate;

    private ImaggaHandler imaggaHandler;

    @BeforeEach
    void setup() {
        imaggaHandler = new ImaggaHandler(restTemplate);
    }

    @Test
    @DisplayName("start uri build should construct base URL")
    void startUriBuildShouldConstructBaseURL() {
        Assertions.assertEquals("https://api.imagga.com/v2/X", ImaggaHandler.startURIBuild("X").toUriString());
    }

    @Test
    @DisplayName("update headers with auth should set BASIC auth")
    void updateHeadersWithAuthShouldSetBASICAuth() {
        HttpHeaders headers = new HttpHeaders();
        ImaggaHandler.updateHeadersWithAuth(headers);
        Assertions.assertEquals("Basic YWNjXzc2MGNhOGExZDE3Mzc2YTo4NzE2N2NhOGY4ZjIwOWQxN2I5NDNiMjQzNmRmOWUyNA==",
                headers.getFirst("Authorization"));
    }

    @Test
    @DisplayName("upload image should throw tagging exception if upload ID not available")
    void uploadImageShouldThrowTaggingExceptionIfUploadIdNotAvailable() {
        ResponseEntity<ImaggaUploadResponse> response = new ResponseEntity<>(
                ImaggaUploadResponse.builder().build(), HttpStatus.OK
        );
        Mockito.lenient().when(restTemplate.postForEntity(
                Mockito.any(),
                Mockito.any(),
                Mockito.eq(ImaggaUploadResponse.class)))
                .thenReturn(response);

        Assertions.assertThrows(TaggingException.class, () -> imaggaHandler.uploadImage(new byte[0]));
    }

    @Test
    @DisplayName("upload image should return upload ID")
    void uploadImageShouldReturnUploadID() {
        ResponseEntity<ImaggaUploadResponse> response = new ResponseEntity<>(
                ImaggaUploadResponse.builder()
                        .result(new ImaggaUploadResult("ID"))
                        .build(), HttpStatus.OK
        );
        Mockito.lenient().when(restTemplate.postForEntity(
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.eq(ImaggaUploadResponse.class)))
                .thenReturn(response);

        Assertions.assertEquals("ID", imaggaHandler.uploadImage(new byte[0]));
    }

    @Test
    @DisplayName("handle upload URI")
    void handleUploadURI() {
        ResponseEntity<ImaggaUploadResponse> response = new ResponseEntity<>(
                ImaggaUploadResponse.builder()
                        .result(new ImaggaUploadResult("ID"))
                        .build(), HttpStatus.OK
        );
        Mockito.lenient().when(restTemplate.postForEntity(
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.eq(ImaggaUploadResponse.class)))
                .thenReturn(response);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString("https://test")
                .pathSegment("test");

        URI uri = imaggaHandler.handleUpload(builder, new byte[0]);

        Assertions.assertEquals("https://test/test?image_upload_id=ID", uri.toString());
    }

    @Test
    @DisplayName("handle image url URI ")
    void handleImageUrlURI() {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString("https://test")
                .pathSegment("test");

        URI uri = imaggaHandler.handleURL(builder, "http://x.x");

        Assertions.assertEquals("https://test/test?image_url=http%3A%2F%2Fx.x", uri.toString());
    }

    @Test
    @DisplayName("should call upload if image available")
    void shouldCallUploadIfImageAvailable() throws URISyntaxException {
        ResponseEntity<ImaggaUploadResponse> response = new ResponseEntity<>(
                ImaggaUploadResponse.builder()
                        .result(new ImaggaUploadResult("ID"))
                        .build(), HttpStatus.OK
        );
        Mockito.lenient().when(restTemplate.postForEntity(
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.eq(ImaggaUploadResponse.class)))
                .thenReturn(response);

        Mockito.when(restTemplate.exchange(
                Mockito.eq(new URI("https://api.imagga.com/v2/tags?image_upload_id=ID")),
                Mockito.any(),
                Mockito.any(),
                Mockito.eq(ImaggaTagResponse.class)))
                .thenReturn(new ResponseEntity<>(ImaggaTagResponse.builder().build(), HttpStatus.OK));

        ImageSubmission submission = ImageSubmission.builder().image(new byte[0]).build();

        imaggaHandler.findObjects(submission);

        Mockito.verify(restTemplate, Mockito.atLeastOnce()).postForEntity(
                Mockito.any(),
                Mockito.any(),
                Mockito.eq(ImaggaUploadResponse.class));

        Mockito.verify(restTemplate, Mockito.atLeastOnce()).exchange(
                Mockito.eq(new URI("https://api.imagga.com/v2/tags?image_upload_id=ID")),
                Mockito.any(),
                Mockito.any(),
                Mockito.eq(ImaggaTagResponse.class));
    }

    @Test
    @DisplayName("should pass image url if url provided")
    void shouldPassImageUrlIfUrlProvided() throws URISyntaxException {
        Mockito.when(restTemplate.exchange(
                        Mockito.eq(new URI("https://api.imagga.com/v2/tags?image_url=http%3A%2F%2Fx.x")),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.eq(ImaggaTagResponse.class)))
                .thenReturn(new ResponseEntity<>(ImaggaTagResponse.builder().build(), HttpStatus.OK));

        ImageSubmission submission = ImageSubmission.builder().imageUrl("http://x.x").build();

        imaggaHandler.findObjects(submission);

        Mockito.verify(restTemplate, Mockito.atLeastOnce()).exchange(
                Mockito.eq(new URI("https://api.imagga.com/v2/tags?image_url=http%3A%2F%2Fx.x")),
                Mockito.any(),
                Mockito.any(),
                Mockito.eq(ImaggaTagResponse.class));
    }

    @Test
    @DisplayName("should map tags to set of strings")
    void shouldMapTagsToSetOfStrings() throws URISyntaxException {
        Mockito.when(restTemplate.exchange(
                        Mockito.eq(new URI("https://api.imagga.com/v2/tags?image_url=http%3A%2F%2Fx.x")),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.eq(ImaggaTagResponse.class)))
                .thenReturn(new ResponseEntity<>(ImaggaTagResponse.builder()
                        .result(
                                ImaggaTagList.builder()
                                        .tags(
                                                List.of(
                                                        new ImaggaTag(1.0, new ImaggaTagValue("X")),
                                                        new ImaggaTag(1.0, new ImaggaTagValue("Z"))
                                                )
                                        )
                                        .build()
                        )
                        .build(), HttpStatus.OK));

        ImageSubmission submission = ImageSubmission.builder().imageUrl("http://x.x").build();

        Set<String> objects = imaggaHandler.findObjects(submission);

        Assertions.assertEquals(Set.of("X", "Z"), objects);
    }

    @Test
    @DisplayName("should be able to deserialize tag response from JSON")
    void shouldBeAbleToDeserializeTagResponseFromJSON() throws JsonProcessingException {
        String responseJSON = "{\"result\": {\"tags\": [{\"confidence\": 14.0, \"tag\": {\"en\": \"tag\"}}]}}";

        ImaggaTagResponse response = new ObjectMapper().readValue(responseJSON, ImaggaTagResponse.class);

        Assertions.assertEquals(14.0, response.getResult().getTags().get(0).getConfidence());
        Assertions.assertEquals("tag", response.getResult().getTags().get(0).getTag().getEn());
    }

    @Test
    @DisplayName("should be able to deserialize upload response from JSON")
    void shouldBeAbleToDeserializeFromJSON() throws JsonProcessingException {
        String responseJSON = "{\"result\": {\"uploadId\": \"ID\"}, \"status\": {\"type\": \"OK\"}}";

        ImaggaUploadResponse response = new ObjectMapper().readValue(responseJSON, ImaggaUploadResponse.class);

        Assertions.assertEquals("OK", response.getStatus().getType());
        Assertions.assertEquals("ID", response.getResult().getUploadId());
    }

}
