package com.example.springreactive.service

import com.example.springreactive.*
import com.example.springreactive.model.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class SentimentAnalysisServiceTest {
    private lateinit var sentimentAnalysisService: SentimentAnalysisService
    private val havenApiConfig: HavenApiConfig = havenApiConfig()
    private val webClient = mock<CustomWebClient>()
    private val responseSpec = mock<WebClient.ResponseSpec>()

    val text = "I fell great today"

    @Before
    fun setup() {
        sentimentAnalysisService = SentimentAnalysisService(havenApiConfig, webClient)
    }

    @Test
    fun `Should get personal report successfully`() {
        val sentimentAnalysis2: SentimentAnalysis = sentimentAnalysis(positive = listOf(
                sentiment(sentiment = TestConstants.SENTIMENT2)))
        val sentimentResponse: Mono<SentimentResponse> = Mono.just(sentimentResponse(
                sentiment_analysis = listOf(sentimentAnalysis(), sentimentAnalysis2)))

        whenever(webClient.retrieveSentimentAnalysis(eq(havenApiConfig), any())).thenReturn(responseSpec)
        whenever(responseSpec.bodyToMono(eq(SentimentResponse::class.java))).thenReturn(sentimentResponse)

        StepVerifier.create(sentimentAnalysisService.getSentimentAnalysis(text))
                .expectNext(sentimentAnalysis())
                .expectNext(sentimentAnalysis2)
                .expectComplete()
                .verify()
    }

    @Test
    fun `Throw exception with invalid api key`() {
        val sentimentResponse: Mono<SentimentResponse> = Mono.error(webClientResponseException())

        whenever(webClient.retrieveSentimentAnalysis(eq(havenApiConfig), any())).thenReturn(responseSpec)
        whenever(responseSpec.bodyToMono(eq(SentimentResponse::class.java))).thenReturn(sentimentResponse)

        StepVerifier.create(sentimentAnalysisService.getSentimentAnalysis(text))
                .verifyError()
    }

}