package com.Bank.AccountHub.Services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private final RestTemplate restTemplate;

    public ExternalApiService() {
        this.restTemplate = new RestTemplate();
    }

    public String logToExternalSystem(int statusCode) {
        String url = "https://httpstat.us/" + statusCode;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
}