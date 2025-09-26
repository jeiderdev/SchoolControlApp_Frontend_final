package com.example.schoolcontrol.models

import com.google.gson.annotations.SerializedName

data class CreateEnrollmentDto(
    @SerializedName("student_id") val studentId: Int,
    @SerializedName("subject_id") val subjectId: Int
)
