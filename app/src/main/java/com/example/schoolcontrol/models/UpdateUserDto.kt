package com.example.schoolcontrol.models

data class UpdateUserDto(
    val name: String? = null,
    val age: Int? = null,
    val password: String? = null,
    val photo: String? = null,
    val active: Boolean? = null
)