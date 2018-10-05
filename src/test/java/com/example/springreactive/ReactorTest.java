package com.example.springreactive;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class ReactorTest {
    private static Logger logger = LoggerFactory.getLogger(ReactorTest.class);

    public <T> Flux<T> appendBoomError(Flux<T> source) {
        return source.concatWith(Mono.error(new IllegalArgumentException("boom")));
    }

    @Test
    public void testSorting() throws InterruptedException {
        Flux.just(9, 3, 1, 4, 6, 2)
                .sort()
                .last()
                .subscribe(System.out::println);

        Thread.currentThread().sleep(2000);
    }

    @Test
    public void testAppendBoomError() {
        Flux<String> source = Flux.just("foo", "bar");

        StepVerifier.create(appendBoomError(source))
                .expectNext("foo")
                .expectNext("bar")
                .expectErrorMessage("boom")
                .verify();
    }

    @Test
    public void testSubscribeOn() throws InterruptedException {
        Scheduler s = Schedulers.newParallel("sched1",4);

        final Flux<String> flux = Flux
                .range(1, 3)
                .map(i -> 10 + i)
                .map(i -> i + Thread.currentThread().getName())
                .subscribeOn(s)
                .map(i -> "value: " + i + ":" + Thread.currentThread().getName());

        Thread t = new Thread(() -> flux.subscribe(System.out::println));
        t.start();
        logger.info("waiting thread: " + Thread.currentThread().getName());
        t.join();

        Thread.currentThread().sleep(2000);
    }

    @Test
    public void testPublishOn() throws InterruptedException {
        Scheduler s = Schedulers.newParallel("sched1", 4);

        final Flux<String> flux = Flux
                .range(1, 3)
                .map(i -> 10 + i)
                .map(i -> i + Thread.currentThread().getName())
                .publishOn(s)
                .map(i -> "value: " + i + ":" + Thread.currentThread().getName());

        Thread t = new Thread(() -> flux.subscribe(System.out::println));
        t.start();
        logger.info("waiting thread: " + Thread.currentThread().getName());
        t.join();

    }


}
