package com.corvit.corvit_lms.data

data class Course(
    val name: String = "",
    val category_id: String = "",
    val certification: Boolean = false,

    val courseLevel: String? = null,
    val duration: String? = null,
    val batchType: String? = null,
    val vendor: String? = null,

    val prices: PriceDetails? = null
)

data class PriceDetails(
    val regular_pkr: Double? = null,
    val discount_pkr: Double? = null,

    val group_usd: Double? = null,
    val one_to_one_usd: Double? = null
)

enum class CourseFilter {
    ALL,
    PRICE_LOW_HIGH,
    PRICE_HIGH_LOW,
    FREE,
    CERTIFIED,
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}

