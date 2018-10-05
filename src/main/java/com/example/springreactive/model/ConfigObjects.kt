package com.example.springreactive.model

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@Component
@NoArg
@ConfigurationProperties("astroloy-api")
data class AstrologyApiConfig(
        var url: String? = null,
        var personalityPath: String? = null,
        var userId: String? = null,
        var apiKey: String? = null
)

@Component
@NoArg
@ConfigurationProperties(prefix = "haven-api")
data class HavenApiConfig(
        var url: String? = null,
        var sentimentPath: String? = null,
        var apiKey: String? = null
)