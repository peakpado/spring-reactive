package com.example.springreactive

import com.example.springreactive.data.ReactiveUserRepository
import com.example.springreactive.model.Aggregate
import com.example.springreactive.service.SentimentOrchestrator
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.mockito.BDDMockito.given
import org.springframework.http.MediaType
import reactor.core.publisher.Mono
import com.example.springreactive.TestConstants.USERNAME
import com.example.springreactive.model.ErrorResponse
import com.example.springreactive.model.User
import reactor.core.publisher.Flux
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@WebFluxTest(ReactiveController::class)
class ReactiveControllerTest {

    @Autowired
    private lateinit var webClient: WebTestClient

    @MockBean
    private lateinit var reactiveUserRepository: ReactiveUserRepository
    @MockBean
    private lateinit var sentimentOrchestrator: SentimentOrchestrator

    @Test
    fun `Should get best sentiment successfully`() {
        val aggregate: Mono<Aggregate> = Mono.just(aggregate())
        given(this.sentimentOrchestrator.getBestSentiment(USERNAME)).willReturn(aggregate)

        this.webClient.get().uri("/sentiment/"+ USERNAME)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .returnResult(Aggregate::class.java)
                .responseBody
                .subscribe {
                    assertEquals(aggregate(), it)
                }

    }

    @Test
    fun `Should get list of users successfully`() {
        val userList = listOf(user(), user(username = "seconduser"))
        val users: Flux<User> = Flux.fromIterable(userList)
        given(this.reactiveUserRepository.findAll()).willReturn(users)

        this.webClient.get().uri("/users/")
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .returnResult(User::class.java)
                .responseBody
                .collectList()
                .subscribe {
                    assertEquals(userList, it)
                }

    }

    @Test
    fun `Should create a user successfully`() {
        val notExist: Mono<Boolean> = Mono.just(false)
        val user = user()
        val savedUser: Mono<User> = Mono.just(user)
        given(this.reactiveUserRepository.existsById(USERNAME)).willReturn(notExist)
        given(this.reactiveUserRepository.save(user)).willReturn(savedUser)

        this.webClient.post().uri("/users/")
                .syncBody(user)
                .exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .returnResult(User::class.java)
                .responseBody
                .subscribe {
                    assertEquals(user, it)
                }

    }

    @Test
    fun `Should fail to create a user when user already exists`() {
        val exist: Mono<Boolean> = Mono.just(true)
        val user = user()
        given(this.reactiveUserRepository.existsById(USERNAME)).willReturn(exist)

        this.webClient.post().uri("/users/")
                .syncBody(user)
                .exchange()
                .expectStatus().isBadRequest
                .returnResult(ErrorResponse::class.java)
                .responseBody
                .subscribe{
                    assertEquals("Username ${USERNAME} already exists", it.message)
                }

    }

    @Test
    fun `Should fail to create a user when date of birth is invalid`() {
        val exist: Mono<Boolean> = Mono.just(true)
        val user = user(dateOfBirth = dateOfBirth(month = 13))
        given(this.reactiveUserRepository.existsById(USERNAME)).willReturn(exist)

        this.webClient.post().uri("/users/")
                .syncBody(user)
                .exchange()
                .expectStatus().isBadRequest

    }

}