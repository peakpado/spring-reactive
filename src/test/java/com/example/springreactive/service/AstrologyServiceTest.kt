package com.example.springreactive.service

import com.example.springreactive.TestConstants.REPORT1
import com.example.springreactive.TestConstants.REPORT2
import com.example.springreactive.astrologyApiConfig
import com.example.springreactive.dateOfBirth
import com.example.springreactive.model.AstrologyApiConfig
import com.example.springreactive.model.PersonalityReport
import com.example.springreactive.personalityReport
import com.example.springreactive.webClientResponseException
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class AstrologyServiceTest {
    private lateinit var astrologyService: AstrologyService
    private val astrologyApiConfig: AstrologyApiConfig = astrologyApiConfig()
    private val webClient = mock<CustomWebClient>()
    private val responseSpec = mock<WebClient.ResponseSpec>()

    @Before
    fun setup() {
        astrologyService = AstrologyService(astrologyApiConfig, webClient)
    }

    @Test
    fun `Should get personal report successfully`() {
        val personalityReport: Mono<PersonalityReport> = Mono.just(personalityReport())

        whenever(webClient.retrievePersonalityReport(eq(astrologyApiConfig), any())).thenReturn(responseSpec)
        whenever(responseSpec.bodyToMono(eq(PersonalityReport::class.java))).thenReturn(personalityReport)

        StepVerifier.create(astrologyService.getPersonalityReport(dateOfBirth()))
                .expectNext(REPORT1)
                .expectNext(REPORT2)
                .expectComplete()
                .verify()
    }

    @Test
    fun `Throw error with invalid api key`() {
        val personalityReport: Mono<PersonalityReport> = Mono.error(webClientResponseException())

        whenever(webClient.retrievePersonalityReport(eq(astrologyApiConfig), any())).thenReturn(responseSpec)
        whenever(responseSpec.bodyToMono(eq(PersonalityReport::class.java))).thenReturn(personalityReport)

        StepVerifier.create(astrologyService.getPersonalityReport(dateOfBirth()))
                .verifyError()
    }
}