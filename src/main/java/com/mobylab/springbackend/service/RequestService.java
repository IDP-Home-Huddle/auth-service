package com.mobylab.springbackend.service;

import com.mobylab.springbackend.exception.RequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class RequestService {
    private final RestTemplate restTemplate;

    @Value("${vars.task-management-service-url}")
    private String TASK_MANAGEMENT_SERVICE_URL;

    public RequestService() {
        this.restTemplate = new RestTemplate();
    }

    public <T> ResponseEntity<T> sendGetRequest(String url, Map<String, String> queryParams, ParameterizedTypeReference<T> responseType) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TASK_MANAGEMENT_SERVICE_URL + url);
        queryParams.forEach(builder::queryParam);

        String uri = builder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            return restTemplate.exchange(uri, HttpMethod.GET, requestEntity, responseType);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RequestException(ex.getStatusCode(), ex.getMessage());
        }
    }

    public <R, T> ResponseEntity<T> sendPostRequest(String url, Map<String, String> queryParams, R requestBody, ParameterizedTypeReference<T> responseType) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TASK_MANAGEMENT_SERVICE_URL + url);
        queryParams.forEach(builder::queryParam);

        String uri = builder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<R> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            return restTemplate.exchange(uri, HttpMethod.POST, requestEntity, responseType);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RequestException(ex.getStatusCode(), ex.getMessage());
        }
    }

    public <R, T> ResponseEntity<T> sendPutRequest(String url, Map<String, String> queryParams, R requestBody, ParameterizedTypeReference<T> responseType) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TASK_MANAGEMENT_SERVICE_URL + url);
        queryParams.forEach(builder::queryParam);

        String uri = builder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<R> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            return restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, responseType);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RequestException(ex.getStatusCode(), ex.getMessage());
        }
    }

    public <T> ResponseEntity<T> sendDeleteRequest(String url, Map<String, String> queryParams, ParameterizedTypeReference<T> responseType) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TASK_MANAGEMENT_SERVICE_URL + url);
        queryParams.forEach(builder::queryParam);

        String uri = builder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            return restTemplate.exchange(uri, HttpMethod.DELETE, requestEntity, responseType);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RequestException(ex.getStatusCode(), ex.getMessage());
        }
    }
}
