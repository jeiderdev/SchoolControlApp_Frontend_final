package com.example.schoolcontrol.models

import com.google.gson.annotations.SerializedName

data class CreateEvaluationDto(
    val name: String,
    @SerializedName("subject_id") val subjectId: Int
)