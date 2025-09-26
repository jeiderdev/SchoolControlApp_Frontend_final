package com.example.schoolcontrol.models

data class UserDto(
    val id: Int,
    val name: String,
    val idnumber: String,
    val email: String,
    val age: Int,
    val role: UserRole,
    val photo: String? = null,
    val active: Boolean,
    val subjects: List<SubjectDto> = emptyList(),
    val enrollments: List<EnrollmentDto> = emptyList(),
    val grades: List<GradeDto> = emptyList()
)
