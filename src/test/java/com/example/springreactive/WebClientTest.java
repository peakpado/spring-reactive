package com.example.springreactive;

import com.example.springreactive.data.PersonalityReport;
import com.example.springreactive.data.Profile;
import com.example.springreactive.data.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

public class WebClientTest {
    private Logger logger = LoggerFactory.getLogger(WebClientTest.class);

    @Test
    public void testHavenSentimentAnalysis() throws InterruptedException {

        String text = "this is good";
        String apikey = "375ba53c-5f17-4abc-9fb3-41219e5269cc";

        Mono<PersonalityReport> personality = WebClient.create("https://api.havenondemand.com/1")
                .get()
                .uri("/api/sync/analyzesentiment/v2?apikey={apikey}&text={text}", apikey, text)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(PersonalityReport.class);

        personality.log()
                .subscribe(p -> {
                    logger.info("---------- response --------");
                    logger.info(p.toString());
                });

        Thread.currentThread().sleep(4000);
    }

    @Test
    public void testAstrologyAPI() throws InterruptedException {
        WebClient client = WebClient.builder()
                .baseUrl("https://json.astrologyapi.com/v1")
                .filter(basicAuthentication("602919", "2376ab63f0424bd6f69c1778e2f131a1"))
                .build();

        Profile profile = new Profile(1966, 6, 5, 1, 3, 19.2056, 25.249, 7);

        Mono<PersonalityReport> personality = client.post()
                .uri("/personality_report/tropical")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .syncBody(profile)
                .retrieve()
                .bodyToMono(PersonalityReport.class);

        personality.log()
                .subscribe(p -> {
                    logger.info("---------- response --------");
                    logger.info(p.toString());
                });

        Thread.currentThread().sleep(4000);
    }

    @Test
    public void testMongoClient() throws InterruptedException {
        WebClient client = WebClient.create("http://localhost:8080");

        Flux<User> result = client.get()
                .uri("/mongo/reactive").accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(User.class);

        result.log().subscribe(u -> {
            logger.info(u.getFirstname());
        });
//        result.toStream()
//        .forEach(u -> logger.info(u.getFirstname()));

        Thread.currentThread().sleep(6000);
    }

//    @Test
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
