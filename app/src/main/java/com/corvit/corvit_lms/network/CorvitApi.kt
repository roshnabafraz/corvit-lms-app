package com.corvit.corvit_lms.network

import com.corvit.corvit_lms.data.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CorvitApi {

    @GET("courses")
    suspend fun getAllCourses(): Response<CourseListResponse>

    @GET("courses/{id}")
    suspend fun getCourseDetail(@Path("id") id: Int): Response<CourseDetailResponse>

    @GET("courses/{id}/batches")
    suspend fun getBatches(@Path("id") courseId: Int): Response<BatchListResponse>

    @POST("query")
    suspend fun submitEnrollment(@Body request: EnrollmentRequest): Response<GenericResponse>
}