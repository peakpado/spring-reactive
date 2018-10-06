package com.example.springreactive.service;

import com.example.springreactive.ApiException;
import com.example.springreactive.model.HavenApiConfig;
import com.example.springreactive.model.SentimentAnalysis;
import com.example.springreactive.model.SentimentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class SentimentAnalysisService {
    private static final Logger logger = LoggerFactory.getLogger(SentimentAnalysisService.class);

    private final HavenApiConfig havenApiConfig;
    private final CustomWebClient webClient;

    public SentimentAnalysisService(HavenApiConfig havenApiConfig,
                                    CustomWebClient webClient) {
        this.havenApiConfig = havenApiConfig;
        this.webClient = webClient;
    }

    public Flux<SentimentAnalysis> getSentimentAnalysis(String text) {
        logger.debug("Requesting sentiment analysis for text {}", text);

        return webClient.retrieveSentimentAnalysis(havenApiConfig, text)
                .bodyToMono(SentimentResponse.class)    // Mono<SentimentResponse>
                .flatMapMany(response -> Flux.fromStream(response.getSentiment_analysis().stream()))   // Flux<SentimentAnalysis>
                .log()
                .onErrorMap(ex -> new ApiException("Failed to access SentimentAnalysis service", HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    public Flux<SentimentAnalysis> getSentimentAnalysisOriginal(String text) {
        logger.debug("Requesting sentiment analysis for text {}", text);

        return WebClient.create(havenApiConfig.getUrl())
                .get()
                .uri(havenApiConfig.getSentimentPath() + "?apikey={apikey}&text={text}", havenApiConfig.getApiKey(), text)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SentimentResponse.class)    // Mono<SentimentResponse>
                .flatMapMany(response -> Flux.fromStream(response.getSentiment_analysis().stream()))   // Flux<SentimentAnalysis>
                .log()
                .onErrorMap(ex -> new ApiException("Failed to access SentimentAnalysis service", HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }
}
