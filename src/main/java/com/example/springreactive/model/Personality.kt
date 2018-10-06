package com.example.springreactive.model

@NoArg
data class PersonalityReport(
        val report: List<String> = listOf(),
        val spiritual_lesson: String,
        val key_quality: String
)