package com.example.springreactive.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
        @Id
//        val id: String,
        val username: String,
        val firstname: String,
        val lastname: String,
        val dateOfBirth: DateOfBirth
)

data class DateOfBirth(
        val year: Int,
        val month: Int,
        val day: Int,
        val hour: Int,
        val min: Int,
        val lat: Double,
        val lon: Double,
        val tzone: Double
)