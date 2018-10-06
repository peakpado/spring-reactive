package com.example.springreactive.service;

import com.example.springreactive.ApiException;
import com.example.springreactive.data.ReactiveUserRepository;
import com.example.springreactive.model.Aggregate;
import com.example.springreactive.model.DateOfBirth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
public class SentimentOrchestrator {
    private static final Logger logger = LoggerFactory.getLogger(SentimentOrchestrator.class);

    private final ReactiveUserRepository reactiveUserRepository;
    private final AstrologyService astrologyService;
    private final SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    public SentimentOrchestrator(ReactiveUserRepository reactiveUserRepository,
                                 AstrologyService astrologyService,
                                 SentimentAnalysisService sentimentAnalysisService) {
        this.reactiveUserRepository = reactiveUserRepository;
        this.astrologyService = astrologyService;
        this.sentimentAnalysisService = sentimentAnalysisService;
    }

    public Mono<Aggregate> getBestSentiment(String username) {
        logger.debug("Requesting best sentiment for user {}", username);

        return reactiveUserRepository.findByUsername(username)
                .single()       // Mono<User>
                .log()
                .onErrorMap(ex -> new ApiException("User " + username + " is not found", HttpStatus.NOT_FOUND))
                .map(user -> user.getDateOfBirth())         // Mono<DateOfBirth>
                .flatMap(this::getHighestSentimentAggregate);   // Mono<Aggregate>
    }

    private Mono<Aggregate> getHighestSentimentAggregate(DateOfBirth dateOfBirth) {
        return astrologyService.getPersonalityReport(dateOfBirth)  // Flux<String>
                .flatMap(report -> sentimentAnalysisService.getSentimentAnalysis(report)) // Flux<SentimentAnalysis>
                .map(sentiment -> sentiment.getAggregate())     // Flux<Aggregate>
                .log()
                .sort(Comparator.comparing(Aggregate::getScore))    // sort
                .last();
//                .onErrorMap(ex -> new ApiException("No sentiment is returned", HttpStatus.INTERNAL_SERVER_ERROR, ex));    // get the highest score.
    }
}
