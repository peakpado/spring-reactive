package com.example.springreactive.service

import com.example.springreactive.TestConstants.REPORT1
import com.example.springreactive.TestConstants.REPORT2
import com.example.springreactive.TestConstants.USERNAME
import com.example.springreactive.aggregate
import com.example.springreactive.data.ReactiveUserRepository
import com.example.springreactive.dateOfBirth
import com.example.springreactive.model.User
import com.example.springreactive.sentimentAnalysis
import com.example.springreactive.user
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class SentimentOrchestratorTest{
    private lateinit var sentimentOrchestrator: SentimentOrchestrator

    private val reactiveUserRepository = mock<ReactiveUserRepository>()
    private val astrologyService = mock<AstrologyService>()
    private val sentimentAnalysisService = mock<SentimentAnalysisService>()

    @Before
    fun setup() {
        sentimentOrchestrator = SentimentOrchestrator(reactiveUserRepository, astrologyService, sentimentAnalysisService)
    }

    @Test
    fun `Should get best sentiment successfully`() {
        val user: Mono<User> = Mono.just(user())
        whenever(reactiveUserRepository.findByUsername(USERNAME)).thenReturn(user)

        val reports: Flux<String> = Flux.just(REPORT1, REPORT2)
        whenever(astrologyService.getPersonalityReport(eq(dateOfBirth()))).thenReturn(reports)

        val sentimentAnalysis = sentimentAnalysis()
        val sentimentAnalysis2 = sentimentAnalysis(aggregate = aggregate(score = 56.5))
        val sentimentAnalyses = Flux.just(sentimentAnalysis, sentimentAnalysis2)
        whenever(sentimentAnalysisService.getSentimentAnalysis(REPORT1)).thenReturn(sentimentAnalyses)

        val sentimentAnalysis3 = sentimentAnalysis(aggregate = aggregate(score = 95.3))
        val sentimentAnalysis4 = sentimentAnalysis(aggregate = aggregate(score = 45.7))
        val sentimentAnalyses2 = Flux.just(sentimentAnalysis3, sentimentAnalysis4)
        whenever(sentimentAnalysisService.getSentimentAnalysis(REPORT2)).thenReturn(sentimentAnalyses2)

        StepVerifier.create(sentimentOrchestrator.getBestSentiment(USERNAME))
                .expectNext(aggregate())
                .expectComplete()
                .verify()
    }

    @Test
    fun `Throw error when user is not found`() {
        val user: Mono<User> = Mono.empty()
        whenever(reactiveUserRepository.findByUsername(USERNAME)).thenReturn(user)

        StepVerifier.create(sentimentOrchestrator.getBestSentiment(USERNAME))
                .expectError()
                .verify()
    }

    @Test
    fun `Throw error when astrology returns empty report`() {
        val user: Mono<User> = Mono.just(user())
        whenever(reactiveUserRepository.findByUsername(USERNAME)).thenReturn(user)

        val reports: Flux<String> = Flux.empty()
        whenever(astrologyService.getPersonalityReport(eq(dateOfBirth()))).thenReturn(reports)

        StepVerifier.create(sentimentOrchestrator.getBestSentiment(USERNAME))
                .expectError()
                .verify()
    }

}