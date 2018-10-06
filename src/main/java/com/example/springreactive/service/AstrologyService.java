package com.example.springreactive.service;

import com.example.springreactive.ApiException;
import com.example.springreactive.model.AstrologyApiConfig;
import com.example.springreactive.model.DateOfBirth;
import com.example.springreactive.model.PersonalityReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@Service
public class AstrologyService {
    private static final Logger logger = LoggerFactory.getLogger(AstrologyService.class);

    private final AstrologyApiConfig astrologyApiConfig;
    private final CustomWebClient webClient;

    public AstrologyService(AstrologyApiConfig astrologyApiConfig,
                            CustomWebClient webClient) {
        this.astrologyApiConfig = astrologyApiConfig;
        this.webClient = webClient;
    }

    public Flux<String> getPersonalityReport(DateOfBirth dateOfBirth) {
        logger.debug("Requesting personality report for {}", dateOfBirth);

        return webClient.retrievePersonalityReport(astrologyApiConfig, dateOfBirth)
                .bodyToMono(PersonalityReport.class)
                .log()
                .flatMapMany(p -> Flux.fromStream(p.getReport().stream()))
                .onErrorMap(ex -> new ApiException("Failed to access Astrology service", HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    public Flux<String> getPersonalityReportOriginal(DateOfBirth dateOfBirth) {
        logger.debug("Requesting personality report for {}", dateOfBirth);

        WebClient client = WebClient.builder()
                .baseUrl(astrologyApiConfig.getUrl())
                .filter(basicAuthentication(astrologyApiConfig.getUserId(), astrologyApiConfig.getApiKey()))
                .build();

        return client.post()
                .uri(astrologyApiConfig.getPersonalityPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .syncBody(dateOfBirth)
                .retrieve()
                .bodyToMono(PersonalityReport.class)
                .log()
                .flatMapMany(p -> Flux.fromStream(p.getReport().stream()))
                .onErrorMap(ex -> new ApiException("Failed to access Astrology service", HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }
}
