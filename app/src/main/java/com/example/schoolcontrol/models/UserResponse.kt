package com.example.schoolcontrol.models
data class UserResponse(
    val id: Int,
    val email: String,
    val full_name: String,
    val identification_number: String,
    val age: Int,
    val role: String,
    val is_active: Boolean,
    val photo: String?,
    val password: String?
)
