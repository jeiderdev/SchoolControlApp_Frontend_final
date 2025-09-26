package com.example.schoolcontrol.models
data class UserCreateRequest(
    val email: String,
    val password: String,
    val full_name: String,
    val identification_number: String,
    val age: Int,
    val role: String
)
