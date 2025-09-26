package com.example.schoolcontrol.models

import com.google.gson.annotations.SerializedName

data class CreateSubjectDto(
    val name: String,
    val description: String? = null,
    @SerializedName("teacher_id")
    val teacherId: Int? = null
)
