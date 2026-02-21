package com.corvit.corvit_lms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corvit.corvit_lms.data.ApiCourse
import com.corvit.corvit_lms.data.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val courseRepository: CourseRepository = CourseRepository()
) : ViewModel() {

    private val _coursesList = MutableStateFlow<List<ApiCourse>>(emptyList())
    val coursesList = _coursesList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchCourses()
    }

    fun fetchCourses() {
        viewModelScope.launch {
            _isLoading.value = true
            courseRepository.fetchCourses()
                .onSuccess { courses ->
                    _coursesList.value = courses
                }
                .onFailure {
                    // Handle failure if needed, potentially with a UI state map
                }
            _isLoading.value = false
        }
    }
}