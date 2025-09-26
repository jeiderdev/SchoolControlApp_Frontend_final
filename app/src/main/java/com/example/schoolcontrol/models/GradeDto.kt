package com.example.schoolcontrol.models

import java.time.LocalDateTime

data class GradeDto(
    val id: Int,
    val studentId: Int,
    val evaluationId: Int,
    val score: Float,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val student: UserDto? = null,
    val evaluation: EvaluationDto? = null
)
