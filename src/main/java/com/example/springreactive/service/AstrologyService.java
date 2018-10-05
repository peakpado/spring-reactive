package com.example.springreactive.service;

import com.example.springreactive.data.ReactiveUserRepository;
import com.example.springreactive.model.AstrologyApiConfig;
import com.example.springreactive.model.DateOfBirth;
import com.example.springreactive.model.PersonalityReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@Service
public class AstrologyService {
    private static final Logger logger = LoggerFactory.getLogger(AstrologyService.class);

    private final AstrologyApiConfig astrologyApiConfig;
    private final ReactiveUserRepository reactiveUserRepository;

    public AstrologyService(AstrologyApiConfig astrologyApiConfig,
                            ReactiveUserRepository reactiveUserRepository) {
        this.astrologyApiConfig = astrologyApiConfig;
        this.reactiveUserRepository = reactiveUserRepository;
    }


    public Flux<String> getPersonalityReport(String username) {
//        Mono<DateOfBirth> dob = reactiveUserRepository.findByUsername(username)
//                .map(user -> user.getDateOfBirth());
        DateOfBirth dateOfBirth = new DateOfBirth(1966, 6, 5, 1, 3, 19.2056, 25.249, 7);
        Mono<DateOfBirth> dob = Mono.just(dateOfBirth);

        WebClient client = WebClient.builder()
                .baseUrl(astrologyApiConfig.getUrl())
                .filter(basicAuthentication(astrologyApiConfig.getUserId(), astrologyApiConfig.getApiKey()))
                .build();

        Mono<PersonalityReport> personalityReport = client.post()
                .uri(astrologyApiConfig.getPersonalityPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .syncBody(dateOfBirth)
                .retrieve()
                .bodyToMono(PersonalityReport.class)
                .log();

        Flux<String> reports = personalityReport.flatMapMany(p -> Flux.fromStream(p.getReport().stream()));

        return reports;
    }
}
