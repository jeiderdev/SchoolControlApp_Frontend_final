package com.example.schoolcontrol.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class EnrollmentDto(
    val id: Int,
    val studentId: Int,
    val subjectId: Int,
    val active: Boolean,
    @SerializedName("enrolled_at")
    val enrolledAt: LocalDateTime,
    @SerializedName("updated_at")
    val updatedAt: LocalDateTime,
    val student: UserDto? = null,
    val subject: SubjectDto? = null
)
