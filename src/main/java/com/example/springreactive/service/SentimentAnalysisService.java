package com.example.springreactive.service;

import com.example.springreactive.model.HavenApiConfig;
import com.example.springreactive.model.SentimentAnalysis;
import com.example.springreactive.model.SentimentResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class SentimentAnalysisService {

    private final HavenApiConfig havenApiConfig;

    public SentimentAnalysisService(HavenApiConfig havenApiConfig) {
        this.havenApiConfig = havenApiConfig;
    }

    public Flux<SentimentAnalysis> getSentimentAnalysis(String text) {
        return WebClient.create(havenApiConfig.getUrl())
                .get()
                .uri(havenApiConfig.getSentimentPath() +"?apikey={apikey}&text={text}", havenApiConfig.getApiKey(), text)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SentimentResponse.class)    // Mono<SentimentResponse>
                .flatMapMany(response -> Flux.fromStream(response.getSentiment_analysis().stream()));   // Flux<SentimentAnalysis>
    }

}
