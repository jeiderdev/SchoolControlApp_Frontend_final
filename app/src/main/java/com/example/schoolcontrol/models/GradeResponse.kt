package com.example.schoolcontrol.models
data class GradeResponse(
    val id: Int,
    val student_id: Int,
    val teacher_id: Int,
    val subject: String,
    val grade_value: Double,
    val trimester: Int,
    val created_at: String? = null,
    val updated_at: String? = null
)
