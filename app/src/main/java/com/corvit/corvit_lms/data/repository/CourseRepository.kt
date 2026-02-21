package com.corvit.corvit_lms.data.repository

import com.corvit.corvit_lms.data.ApiCourse
import com.corvit.corvit_lms.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CourseRepository {

    suspend fun fetchCourses(): Result<List<ApiCourse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getAllCourses()
                if (response.isSuccessful && response.body()?.status == true) {
                    Result.success(response.body()!!.courses)
                } else {
                    Result.failure(Exception("Failed to load courses or API status was false."))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
