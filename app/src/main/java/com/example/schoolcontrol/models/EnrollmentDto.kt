package com.example.schoolcontrol.models

import com.google.gson.annotations.SerializedName

data class EnrollmentDto(
    val id: Int,
    val studentId: Int,
    val subjectId: Int,
    val active: Boolean,
    @SerializedName("enrolled_at")
    val enrolledAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val student: UserDto? = null,
    val subject: SubjectDto? = null
)
