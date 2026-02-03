package com.corvit.corvit_lms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corvit.corvit_lms.data.ApiCourse
import com.corvit.corvit_lms.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {

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
            try {
                val response = RetrofitClient.api.getAllCourses()
                if (response.isSuccessful && response.body()?.status == true) {
                    _coursesList.value = response.body()!!.courses
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}