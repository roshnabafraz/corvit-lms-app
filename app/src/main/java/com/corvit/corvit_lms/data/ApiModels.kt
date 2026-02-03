package com.corvit.corvit_lms.data

import com.google.gson.annotations.SerializedName

data class CourseListResponse(
    val status: Boolean,
    val message: String,
    val courses: List<ApiCourse>
)

data class CourseDetailResponse(
    val status: Boolean,
    val message: String,
    val course: ApiCourse
)

data class ApiCourse(
    val id: Int,
    val name: String,
    val description: String?,
    val type: String?,
    val deliveryMode: String?,
    val durationInWeeks: Int?,
    val totalHours: Int?,
    val fee: Double?,
    val maxCapacity: Int?,
    val currentEnrollment: Int?,
    val imagePath: String?,
    val prerequisites: String?,
    val syllabus: String?
) {
    fun getFullImageUrl(): String {
        return if (imagePath.isNullOrEmpty()) ""
        else "https://corvit.analogenterprises.ae$imagePath"
    }
}


data class BatchListResponse(
    val status: Boolean,
    val message: String,
    val batches: List<ApiBatch>
)

data class ApiBatch(
    val id: Int,
    val name: String,
    val courseId: Int,
    val fee: Double?,
    val startDate: String?,
    val endDate: String?,
    val startTime: String?,
    val endTime: String?,
    val daysOfWeek: String?,
    val instructor: ApiInstructor?
) {
    fun formatForDropdown(): String {
        val timeStr = if (startTime != null && endTime != null) "($startTime - $endTime)" else ""
        return "$daysOfWeek: $startDate $timeStr"
    }
}

data class ApiInstructor(
    val id: String,
    val name: String,
    val email: String
)


data class EnrollmentRequest(
    @SerializedName("StudentName") val studentName: String,
    @SerializedName("Phone") val phone: String,
    @SerializedName("CourseId") val courseId: Int,
    @SerializedName("BatchId") val batchId: Int?,
    @SerializedName("Remarks") val remarks: String
)

data class GenericResponse(
    val status: Boolean,
    val message: String
)