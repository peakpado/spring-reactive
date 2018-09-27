package com.example.springreactive;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class ReactiveController {
//    private final MyReactiveLibrary reactiveLibrary;

    //Note Spring Boot 4.3+ autowires single constructors now
//    public ExampleController(MyReactiveLibrary reactiveLibrary) {
//        this.reactiveLibrary = reactiveLibrary;
//    }

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
    public Flux<String> hey(@RequestBody Mono<Person> body) {
        return Mono.just("Hey mister ")
                .concatWith(body
//                        .flatMap(sir -> Flux.fromArray(sir.getLastName().split("")))
                        .map(p -> p.getFirstName())
                        .map(String::toUpperCase)
//                        .take(1)
                ).concatWith(Mono.just(". how are you?"));
    }
}
