package com.example.springreactive;

import com.example.springreactive.data.ReactiveUserRepository;
import com.example.springreactive.model.Aggregate;
import com.example.springreactive.model.DateOfBirth;
import com.example.springreactive.model.User;
import com.example.springreactive.service.SentimentOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;

@RestController
public class ReactiveController {

//    @Autowired
//    private UserRepository userRepository;

    private final ReactiveUserRepository reactiveUserRepository;
    private final SentimentOrchestrator sentimentOrchestrator;

    @Autowired
    public ReactiveController(ReactiveUserRepository reactiveUserRepository,
                              SentimentOrchestrator sentimentOrchestrator) {
        this.reactiveUserRepository = reactiveUserRepository;
        this.sentimentOrchestrator = sentimentOrchestrator;
    }

//    @GetMapping("mongo")
//    public Iterable<User> mongoPerson() {
//        return userRepository.findAll();
//    }

    @GetMapping("/sentiment/{username}")
    public Mono<Aggregate> getSentiment(@PathVariable String username) {
        return sentimentOrchestrator.getBestSentiment(username);
    }

    @PostMapping("/users")
    public Mono<User> createUser(@RequestBody User dto) {
//        return dto.subscribe(u -> reactiveUserRepository.save(u));
        return reactiveUserRepository.save(dto);
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
