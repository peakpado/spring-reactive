package com.example.springreactive;

import com.example.springreactive.data.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class WebClientTest {
    private Logger logger = LoggerFactory.getLogger(WebClientTest.class);

    @Test
    public void testMongoClient() throws InterruptedException {
        WebClient client = WebClient.create("http://localhost:8080");

        Flux<User> result = client.get()
                .uri("/mongo/reactive").accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(User.class);
        result.subscribe(u -> logger.info(u.getFirstname()));

        Thread.currentThread().sleep(2000);
    }

    @Test
    public void testClient() throws InterruptedException {
        WebClient.create("https://www.google.com")
                .get()
//                .uri("/new/shuffle?deck_count=1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .map(resp -> resp.bodyToMono(String.class))
        .subscribe(System.out::println);

        Thread.currentThread().sleep(2000);
    }
}
