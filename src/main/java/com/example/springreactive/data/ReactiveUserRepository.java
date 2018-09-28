package com.example.springreactive.data;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveUserRepository extends ReactiveCrudRepository<User, String> {

    Flux<User> findByLastname(String lastname);

    @Query("{ 'firstname': ?0, 'lastname': ?1}")
    Mono<User> findByFirstnameAndLastname(String firstname, String lastname);

    // Accept parameter inside a reactive type for deferred execution
    Flux<User> findByLastname(Mono<String> lastname);

    Mono<User> findByFirstnameAndLastname(Mono<String> firstname, String lastname);

    @Tailable  // Use a tailable cursor
    Flux<User> findWithTailableCursorBy();
}