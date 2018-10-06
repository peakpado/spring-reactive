package com.example.springreactive

import com.example.springreactive.TestConstants.API_KEY
import com.example.springreactive.TestConstants.ASTROLOGY_URL
import com.example.springreactive.TestConstants.BEST_SCORE
import com.example.springreactive.TestConstants.REPORT1
import com.example.springreactive.TestConstants.REPORT2
import com.example.springreactive.TestConstants.SENTIMENT1
import com.example.springreactive.TestConstants.SENTIMENT2
import com.example.springreactive.TestConstants.SENTIMENT_URL
import com.example.springreactive.TestConstants.USERNAME
import com.example.springreactive.TestConstants.USER_ID
import com.example.springreactive.model.*
import org.springframework.web.reactive.function.client.WebClientResponseException

object TestConstants {
    val USERNAME = "testuser"
    val ASTROLOGY_URL = "http://astrology.com/api"
    val SENTIMENT_URL = "http://haven.com/sentiment/api"
    val USER_ID = "userId-1"
    val API_KEY = "apikey-1"

    val SENTIMENT1 = "feel great"
    val SENTIMENT2 = "doing well"
    val BEST_SCORE = 99.99

    val REPORT1 = "You may have had trouble communicating in early life. Perhaps you suffer from feelings of inadequacy. You overcome these feelings through sheer necessity, for you have determination in achieving your goals and purposes in life."
    val REPORT2 = "You will have strong likes and dislikes, and can be very reserved and dignified, though when vexed you are apt to be sharp and sarcastic if not actually cruel. Avoid pride, cultivate sympathy and endeavour to see things from others� standpoints as well as your own."
}

fun user(
        username: String = USERNAME,
        dateOfBirth: DateOfBirth = dateOfBirth()
) = User(username, dateOfBirth)

fun dateOfBirth(
        year: Int = 1970,
        month: Int = 10,
        day: Int = 6,
        hour: Int = 7,
        min: Int = 24,
        lat: Double = 97.386,
        lon: Double = 269.284,
        tzone: Double = 83.297
) = DateOfBirth(year, month, day, hour, min, lat, lon, tzone)

fun astrologyApiConfig(
        url: String = ASTROLOGY_URL,
        personalityPath: String = "path",
        userId: String = USER_ID,
        apiKey: String = API_KEY
) = AstrologyApiConfig(url, personalityPath, userId, apiKey)

fun havenApiConfig(
        url: String = SENTIMENT_URL,
        sentimentPath: String = "path",
        apiKey: String = API_KEY
) = HavenApiConfig(url, sentimentPath, apiKey)

fun personalityReport(
        report: List<String> = listOf(REPORT1, REPORT2),
        spiritual_lesson: String = "Sociability (lighten up)",
        key_quality: String = "good quality"
) = PersonalityReport(report, spiritual_lesson, key_quality)

fun sentimentResponse(
        sentiment_analysis: List<SentimentAnalysis> = listOf(sentimentAnalysis())
) = SentimentResponse(sentiment_analysis)

fun sentimentResponseTwoAnalysis(
        sentiment_analysis: List<SentimentAnalysis> = listOf(
                sentimentAnalysis(),
                sentimentAnalysis(positive = listOf(sentiment(sentiment = SENTIMENT2))))
) = SentimentResponse(sentiment_analysis)

fun sentimentAnalysis(
        positive: List<Sentiment> = listOf(sentiment()),
        negative: List<Sentiment> = listOf(),
        aggregate: Aggregate = aggregate()
) = SentimentAnalysis(positive, negative, aggregate)

fun sentiment(
        sentiment: String = SENTIMENT1,
        topic: String? = null,
        score: Double = 78.35,
        original_text: String = "original",
        original_length: Int = 44,
        normalized_text: String = "normalized",
        normalized_length: Int = 21,
        offset: Int? = 0
) = Sentiment(sentiment, topic, score, original_text, original_length, normalized_text, normalized_length, offset)

fun aggregate(
        sentiment: SentimentType = SentimentType.positive,
        score: Double = BEST_SCORE
) = Aggregate(sentiment, score)

fun webClientResponseException(
        message: String = "Something went wrong",
        statusCode: Int = 401,
        statusText: String = "Unauthorized"
) = WebClientResponseException(message, statusCode, statusText, null, null, null)