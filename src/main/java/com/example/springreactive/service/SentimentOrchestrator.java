package com.example.springreactive.service;

import com.example.springreactive.model.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
public class SentimentOrchestrator {


    private final AstrologyService astrologyService;
    private final SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    public SentimentOrchestrator(AstrologyService astrologyService,
                                 SentimentAnalysisService sentimentAnalysisService) {
        this.astrologyService = astrologyService;
        this.sentimentAnalysisService = sentimentAnalysisService;
    }

    public Mono<Aggregate> getBestSentiment(String username) {
        return astrologyService.getPersonalityReport(username)  // Flux<String>
                .flatMap(report -> sentimentAnalysisService.getSentimentAnalysis(report)) // Flux<SentimentAnalysis>
                .map(sentiment -> sentiment.getAggregate())     // Flux<Aggregate>
                .log()
                .sort(Comparator.comparing(Aggregate::getScore))    // sort
                .last();    // get the highest score
    }
}
