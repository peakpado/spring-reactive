package com.example.springreactive.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
        @Id
        var id: String = "",
        val username: String? = null,
        val firstname: String = "",
        val lastname: String = "",
        val profile: Profile? = null
)

data class Profile(
        val year: Int,
        val month: Int,
        val day: Int,
        val hour: Int,
        val min: Int,
        val lat: Double,
        val lon: Double,
        val tzone: Double
)