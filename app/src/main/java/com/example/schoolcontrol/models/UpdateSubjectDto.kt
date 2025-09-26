package com.example.schoolcontrol.models

import com.google.gson.annotations.SerializedName

data class UpdateSubjectDto(
    val name: String? = null,
    val description: String? = null,
    @SerializedName("teacher_id")
    val teacherId: Int? = null
)