package com.example.schoolcontrol.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class SubjectDto(
    val id: Int,
    val name: String,
    val description: String? = null,
    @SerializedName("teacher_id")
    val teacherId: Int? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val teacher: UserDto? = null,
    val enrollments: List<EnrollmentDto> = emptyList(),
    val evaluations: List<EvaluationDto> = emptyList()
)
