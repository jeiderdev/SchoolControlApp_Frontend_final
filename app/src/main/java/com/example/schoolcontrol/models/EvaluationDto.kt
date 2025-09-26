package com.example.schoolcontrol.models

import com.google.gson.annotations.SerializedName

data class EvaluationDto(
    val id: Int,
    val name: String,
    val description: String?,
    @SerializedName("subject_id")
    val subjectId: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val subject: SubjectDto? = null,
    val grades: List<GradeDto> = emptyList()
)
