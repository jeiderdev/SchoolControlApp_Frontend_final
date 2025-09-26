package com.example.schoolcontrol.models

import java.time.LocalDateTime

data class EnrollmentDto(
    val id: Int,
    val studentId: Int,
    val subjectId: Int,
    val active: Boolean,
    val enrolledAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val student: UserDto? = null,
    val subject: SubjectDto? = null
)
