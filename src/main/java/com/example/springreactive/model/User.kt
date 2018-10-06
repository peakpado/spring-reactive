package com.example.springreactive.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@NoArg
@Document(collection = "users")
data class User(
        @Id
        val username: String,
        val dateOfBirth: DateOfBirth
)

@NoArg
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

//{
////        companion object {
//                @Jvm
//                fun dateValid(): Boolean {

//                }
////        }
//
//}