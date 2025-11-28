package com.corvit.corvit_lms.data

data class Category(
    val category_id: String = "",
    val name: String = ""
)

data class Course(
    val name: String = "",
    val category_id: String = "",
    val certification: Boolean = false
)