package com.corvit.corvit_lms.viewmodel

import androidx.lifecycle.ViewModel
import com.corvit.corvit_lms.data.Category
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CatalogViewModel : ViewModel(){

    private var _categorylist = MutableStateFlow<List<Category>>(emptyList())
    var categorylist = _categorylist.asStateFlow()

    init {
        getCategories()
    }

    fun getCategories(){
        val db = Firebase.firestore

        db.collection("categories").addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                _categorylist.value = snapshot.toObjects(Category::class.java)
            }
        }


    }

}


//class CatalogViewModel : ViewModel() {
//
//    private val _categorylist = MutableStateFlow<List<Category>>(emptyList())
//    val categorylist = _categorylist.asStateFlow()
//
//    init {
//        getCategories()
//    }
//
//    fun getCategories() {
//        val db = Firebase.firestore
//        db.collection("categories")
//            .get()
//            .addOnSuccessListener { snapshot ->
//                _categorylist.value = snapshot.toObjects(Category::class.java)
//            }
//            .addOnFailureListener {
//                Log.e("CatalogVM", "Error fetching categories", it)
//            }
//    }
//
//}
