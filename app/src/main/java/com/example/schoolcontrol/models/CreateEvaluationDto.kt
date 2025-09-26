package com.example.schoolcontrol.models

import com.google.gson.annotations.SerializedName

data class CreateEvaluationDto(
    val name: String,
    val description: String?,
    val percentage: Int,
    @SerializedName("subject_id") val subjectId: Int
)