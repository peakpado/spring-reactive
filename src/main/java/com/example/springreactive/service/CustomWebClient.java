package com.example.springreactive.service;

import com.example.springreactive.model.AstrologyApiConfig;
import com.example.springreactive.model.DateOfBirth;
import com.example.springreactive.model.HavenApiConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

/**
 * This class is purely to mock WebClient for testing. The mocking WebClient will be available Spring 5.1.
 * Once it's available, this class is not needed.
 */
@Component
public class CustomWebClient {

    public WebClient.ResponseSpec retrievePersonalityReport(AstrologyApiConfig astrologyApiConfig, DateOfBirth body) {
        WebClient client = WebClient.builder()
                .baseUrl(astrologyApiConfig.getUrl())
                .filter(basicAuthentication(astrologyApiConfig.getUserId(), astrologyApiConfig.getApiKey()))
                .build();

        return client.post()
                .uri(astrologyApiConfig.getPersonalityPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .syncBody(body)
                .retrieve();
    }

    public WebClient.ResponseSpec retrieveSentimentAnalysis(HavenApiConfig havenApiConfig, String text) {
        return WebClient.create(havenApiConfig.getUrl())
                .get()
                .uri(havenApiConfig.getSentimentPath() + "?apikey={apikey}&text={text}", havenApiConfig.getApiKey(), text)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve();
    }
}
