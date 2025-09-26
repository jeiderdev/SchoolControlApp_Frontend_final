package com.example.schoolcontrol.models

data class GradeCreate(
    val student_id: Int,
    val teacher_id: Int,
    val subject: String,
    val grade_value: Double,
    val trimester: Int? = null
)
