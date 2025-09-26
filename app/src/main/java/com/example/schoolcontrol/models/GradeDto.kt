package com.example.schoolcontrol.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class GradeDto(
    val id: Int,
    @SerializedName("student_id")
    val studentId: Int,
    @SerializedName("evaluation_id")
    val evaluationId: Int,
    val score: Double,
    @SerializedName("created_at")
    val createdAt: LocalDateTime,
    @SerializedName("updated_at")
    val updatedAt: LocalDateTime,
    val student: UserDto? = null,
    val evaluation: EvaluationDto? = null
)
