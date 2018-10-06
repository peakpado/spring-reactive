package com.example.springreactive;

import com.example.springreactive.data.ReactiveUserRepository;
import com.example.springreactive.model.Aggregate;
import com.example.springreactive.model.DateOfBirth;
import com.example.springreactive.model.ErrorResponse;
import com.example.springreactive.model.User;
import com.example.springreactive.service.SentimentOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ReactiveController {
    private static final Logger logger = LoggerFactory.getLogger(ReactiveController.class);

    private final ReactiveUserRepository reactiveUserRepository;
    private final SentimentOrchestrator sentimentOrchestrator;

    @ExceptionHandler
    public ResponseEntity<Error> handle(ApiException ex) {
        // log exception
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity(new ErrorResponse(ex.getMessage()), ex.getHttpStatus());
    }

    @Autowired
    public ReactiveController(ReactiveUserRepository reactiveUserRepository,
                              SentimentOrchestrator sentimentOrchestrator) {
        this.reactiveUserRepository = reactiveUserRepository;
        this.sentimentOrchestrator = sentimentOrchestrator;
    }

    @GetMapping(value = "/sentiment/{username}")
    public Mono<Aggregate> getSentiment(@PathVariable String username) {
        return sentimentOrchestrator.getBestSentiment(username);
    }

    @PostMapping("/users")
    public Mono<User> createUser(@RequestBody User user) {
        // check dateOfBirth is valid
        if (!isDateOfBirthValid(user.getDateOfBirth())) {
            throw new ApiException("The date of birth of user is invalid or one of values is out of range", HttpStatus.BAD_REQUEST);
        }

        // check exist
        return reactiveUserRepository.existsById(user.getUsername())
                .flatMap(exist -> {
                    if (!exist) {
                        return reactiveUserRepository.save(user)
                                .onErrorMap(ex -> new ApiException("Failed to create user " + user.getUsername(),
                                        HttpStatus.INTERNAL_SERVER_ERROR, ex));
                    } else {
                        throw new ApiException("Username " + user.getUsername() + " already exists", HttpStatus.BAD_REQUEST);
                    }
                });
    }

    private boolean isDateOfBirthValid(DateOfBirth d) {
        boolean valid = d.getYear() > 0 &&
                d.getMonth() > 0 && d.getMonth() <= 12 &&
                d.getDay() > 0 && d.getDay() <= 31 &&
                d.getHour() >= 0 && d.getHour() < 24 &&
                d.getMin() >= 0 && d.getMin() < 60;
        if (!valid) {
            return false;
        }

        try {
            LocalDateTime dob = LocalDateTime.of(d.getYear(), d.getMonth(), d.getDay(), d.getHour(), d.getMin());
            return valid && dob.isBefore(LocalDateTime.now());
        } catch (Exception ex) {
            return false;
        }
    }

    @GetMapping("/users")
    public Flux<User> getAllUser() {
        return reactiveUserRepository.findAll()
                .onErrorMap(ex -> new ApiException("Failed to get all usersr", HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    @GetMapping("mongo/reactive")
    public Flux<User> mongoReactive() {
        return reactiveUserRepository.findAll();
    }

    @GetMapping("hello/{who}")
    public Mono<String> hello(@PathVariable String who) {
        return Mono.just(who)
                .map(w -> "Hello " + w + "!");
    }

    @GetMapping("helloDelay/{who}")
    public Mono<String> helloDelay(@PathVariable String who) {
        return Mono.just("Hello " + who + "!!").delayElement(Duration.ofMillis(50));
    }

    @PostMapping("heyMister")
    public Flux<String> hey(@RequestBody Mono<User> body) {
        return Mono.just("Hey mister ")
                .concatWith(body
                                .map(p -> p.getUsername())
                                .map(String::toUpperCase)
//                        .take(1)
                ).concatWith(Mono.just(". how are you?"));
    }



}
