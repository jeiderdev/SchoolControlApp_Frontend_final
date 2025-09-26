package com.example.schoolcontrol.api

import com.example.schoolcontrol.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("users/login")
    suspend fun login(@Body request: LoginDto): TokenDto

    @POST("users/register")
    suspend fun register(@Body body: CreateUserDto): UserDto

    @GET("users/me")
    suspend fun me(): UserDto

    @PUT("users/me")
    suspend fun updateMe(@Body body: UpdateUserDto): UserDto

    @GET("users")
    suspend fun getAllUsers(): List<UserDto>

    @GET("users/teachers")
    suspend fun getTeachers(): List<UserDto>

    @GET("users/students")
    suspend fun getStudents(): List<UserDto>

    @GET("/subjects")
    suspend fun getSubjects(): List<SubjectDto>

    @POST("/subjects")
    suspend fun createSubject(@Body subject: CreateSubjectDto): SubjectDto

    @GET("/subjects/{id}")
    suspend fun getSubjectById(@Path("id") id: Int): SubjectDto

    @PUT("/subjects/{id}")
    suspend fun updateSubject(@Path("id") id: Int, @Body subject: UpdateSubjectDto): SubjectDto

    @POST("/enrollments")
    suspend fun enrollStudent(@Body enrollment: CreateEnrollmentDto): EnrollmentDto

    @GET("/enrollments/subject/{subjectId}")
    suspend fun getEnrollmentsBySubject(@Path("subjectId") subjectId: Int): List<EnrollmentDto>

    @POST("forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): ForgotResponse

    @POST("reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): BasicResponse

    // Grades
    @GET("grades")
    suspend fun getGrades(@Query("student_id") studentId: Int? = null): Response<List<GradeResponse>>

    @POST("grades")
    suspend fun createGrade(@Body body: GradeCreate): Response<GradeResponse>

    @PUT("grades/{id}")
    suspend fun updateGrade(@Path("id") gradeId: Int, @Body grade: GradeCreate): Response<GradeResponse>

    @DELETE("grades/{id}")
    suspend fun deleteGrade(@Path("id") gradeId: Int): Response<BasicResponse>

    @GET("ranking")
    suspend fun getRanking(): Response<List<RankingItem>>

}

