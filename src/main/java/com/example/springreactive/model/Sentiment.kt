package com.example.springreactive.model

@NoArg
data class BestSentiment(
        val originalAstrology: String,
        val score: Double
)

@NoArg
data class SentimentResponse(
    val sentiment_analysis: List<SentimentAnalysis>
)

@NoArg
data class SentimentAnalysis(
        val positive: List<Sentiment> = listOf(),
        val negative: List<Sentiment> = listOf(),
        val aggregate: Aggregate
)

@NoArg
data class Sentiment(
        val sentiment: String,
        val topic: String? = null,
        val score: Double,
        val original_text: String,
        val original_length: Int,
        val normalized_text: String,
        val normalized_length: Int,
        val offset: Int
)

@NoArg
data class Aggregate(
        val sentiment: SentimentType,
        val score: Double
)

enum class SentimentType {
    positive, negative, neutral
}