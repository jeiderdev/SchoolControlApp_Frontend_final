package com.example.schoolcontrol.models

data class InstitutionalEmailRequest(
    val user_id: Int,
    val email: String,
    val password: String
)
