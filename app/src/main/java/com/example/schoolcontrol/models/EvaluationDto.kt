package com.example.schoolcontrol.models

data class EvaluationDto(
    val id: Int,
    val name: String,
    val subjectId: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val subject: SubjectDto? = null,
    val grades: List<GradeDto> = emptyList()
)
