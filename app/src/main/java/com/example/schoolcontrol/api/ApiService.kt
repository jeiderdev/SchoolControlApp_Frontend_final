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

    @GET("/subjects")
    suspend fun getSubjects(): List<SubjectDto>

    @POST("/subjects")
    suspend fun createSubject(@Body subject: CreateSubjectDto): SubjectDto

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

    // Teachers emails (list)
    @GET("teachers/emails")
    suspend fun getTeachersEmails(): Response<List<TeacherEmail>>

    // Institutional emails endpoints (if present in backend)
    @GET("institutional-emails")
    suspend fun getInstitutionalEmails(): Response<List<InstitutionalEmailResponse>>

    @POST("institutional-emails")
    suspend fun createInstitutionalEmail(@Body body: InstitutionalEmailRequest): Response<InstitutionalEmailResponse>

    @DELETE("institutional-emails/{id}")
    suspend fun deleteInstitutionalEmail(@Path("id") id: Int): Response<BasicResponse>

    // Users (admin)
    @GET("users")
    suspend fun getUsers(): Response<List<UserResponse>>

    @PUT("users/{id}/role")
    suspend fun updateUserRole(@Path("id") userId: Int, @Body request: RoleUpdateRequest): Response<UserResponse>

    @PUT("users/{id}/email")
    suspend fun updateUserEmail(@Path("id") userId: Int, @Body payload: Map<String, String>): Response<UserResponse>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: Int): Response<BasicResponse>

    @POST("confirm")
    suspend fun confirmAccount(@Body request: ConfirmRequest): Response<MessageResponse>
}

