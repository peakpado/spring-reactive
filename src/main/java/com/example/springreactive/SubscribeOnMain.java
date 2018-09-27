package com.example.springreactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class SubscribeOnMain {
    private static Logger logger = LoggerFactory.getLogger(SubscribeOnMain.class);

    public static void main(String[] args) throws InterruptedException {
        final Mono<String> mono = Mono.just("hello ");

        new Thread(() ->
                mono
                .map(msg -> msg + "thread ")
                .subscribe(v ->
                        logger.info(v + Thread.currentThread().getName())
                )
        ).start();

//        Thread.currentThread().sleep(3000);
    }
}
