package com.example.schoolcontrol.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class EvaluationDto(
    val id: Int,
    val name: String,
    @SerializedName("subject_id")
    val subjectId: Int,
    @SerializedName("created_at")
    val createdAt: LocalDateTime,
    @SerializedName("updated_at")
    val updatedAt: LocalDateTime,
    val subject: SubjectDto? = null,
    val grades: List<GradeDto> = emptyList()
)
