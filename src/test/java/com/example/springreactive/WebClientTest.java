package com.example.springreactive;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientTest {
    private Logger logger = LoggerFactory.getLogger(WebClientTest.class);

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
