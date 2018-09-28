package com.example.springreactive;

import com.example.springreactive.data.ReactiveUserRepository;
import com.example.springreactive.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;

@RestController
public class ReactiveController {

//    @Autowired
//    private UserRepository userRepository;

    @Autowired
    private ReactiveUserRepository reactiveUserRepository;

//    @GetMapping("mongo")
//    public Iterable<User> mongoPerson() {
//        return userRepository.findAll();
//    }

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
//                        .flatMap(sir -> Flux.fromArray(sir.getLastname().split("")))
                        .map(p -> p.getFirstname())
                        .map(String::toUpperCase)
//                        .take(1)
                ).concatWith(Mono.just(". how are you?"));
    }

}
