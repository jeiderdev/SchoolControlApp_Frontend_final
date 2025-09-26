package com.example.schoolcontrol.models

import com.google.gson.annotations.SerializedName

data class CreateGradeDto(
    @SerializedName("student_id") val studentId: Int,
    @SerializedName("evaluation_id") val evaluationId: Int,
    val score: Float
)