package com.example.schoolcontrol.models
data class ResetPasswordRequest(val token: String, val new_password: String)
