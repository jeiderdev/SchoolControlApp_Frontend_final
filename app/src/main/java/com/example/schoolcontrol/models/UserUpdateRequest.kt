package com.example.schoolcontrol.models

data class UserUpdateRequest(
    val full_name: String? = null,
    val identification_number: String? = null,
    val age: Int? = null,
    val photo: String? = null,
    val password: String? = null
)
