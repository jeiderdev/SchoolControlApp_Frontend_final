package com.example.schoolcontrol.models

data class CreateUserDto(
    val name: String,
    val idnumber: String,
    val email: String,
    val age: Int,
    val role: UserRole,
    val password: String,
    val photo: String? = null,
    val active: Boolean = true
)

