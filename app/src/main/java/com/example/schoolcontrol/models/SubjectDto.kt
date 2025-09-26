package com.example.schoolcontrol.models

import java.time.LocalDateTime

data class SubjectDto(
    val id: Int,
    val name: String,
    val description: String? = null,
    val teacherId: Int? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val teacher: UserDto? = null,
    val enrollments: List<EnrollmentDto> = emptyList(),
    val evaluations: List<EvaluationDto> = emptyList()
)
