package com.example.schoolcontrol.models
import com.google.gson.annotations.SerializedName

enum class UserRole {
    @SerializedName("admin")
    ADMIN,

    @SerializedName("teacher")
    TEACHER,

    @SerializedName("student")
    STUDENT
}