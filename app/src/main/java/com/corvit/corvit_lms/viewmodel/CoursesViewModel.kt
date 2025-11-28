package com.corvit.corvit_lms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corvit.corvit_lms.data.Course
import com.corvit.corvit_lms.data.UiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await // **CRITICAL IMPORT**
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.label.Category

class CoursesViewModel(private val firestore: FirebaseFirestore) : ViewModel() {

    // --- State for Categories List ---
    private val _categoryState = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categoryState: StateFlow<UiState<List<Category>>> = _categoryState.asStateFlow()

    // --- State for Courses List ---
    private val _courseState = MutableStateFlow<UiState<List<Course>>>(UiState.Loading)
    val courseState: StateFlow<UiState<List<Course>>> = _courseState.asStateFlow()

    init {
        // Load categories on initialization
        loadCategories()
    }

    fun loadCategories() {
        _categoryState.value = UiState.Loading
        viewModelScope.launch {
            try {
                // Get the snapshot using .await() extension function
                val snapshot = firestore.collection("Categories").get().await()

                // Map the results to the data model
                val categories = snapshot.toObjects(Category::class.java)

                // Update the state
                _categoryState.value = UiState.Success(categories)

            } catch (e: Exception) {
                // Handle any error during the fetch
                _categoryState.value = UiState.Error(e.localizedMessage ?: "Failed to load categories")
            }
        }
    }

    fun loadCourses(categoryId: String) {
        _courseState.value = UiState.Loading
        viewModelScope.launch {
            try {
                // Query the Courses collection, filter by category_id, and use .await()
                val snapshot = firestore.collection("Courses")
                    .whereEqualTo("category_id", categoryId)
                    .get()
                    .await()

                // Map the results to the data model
                val courses = snapshot.toObjects(Course::class.java)

                // Update the state
                _courseState.value = UiState.Success(courses)

            } catch (e: Exception) {
                // Handle any error during the fetch
                _courseState.value = UiState.Error(e.localizedMessage ?: "Failed to load courses")
            }
        }
    }
}