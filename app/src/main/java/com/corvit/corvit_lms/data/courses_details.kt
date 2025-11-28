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

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}