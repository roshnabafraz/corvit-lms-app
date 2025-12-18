package com.corvit.corvit_lms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _userDataState = MutableLiveData<UserDataState>(UserDataState.Idle)
    val userDataState: LiveData<UserDataState> = _userDataState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        _authState.value = if (auth.currentUser == null) {
            AuthState.Unauthenticated
        } else {
            AuthState.Authenticated
        }
    }

    fun Login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email & Password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something Went Wrong")
                }
            }
    }

    // Save initial user data to Firestore
    private fun saveUserName(userId: String, name: String) {
        val user = hashMapOf(
            "name" to name,
            "email" to auth.currentUser?.email
        )

        firestore.collection("users").document(userId)
            .set(user)
            .addOnFailureListener {
                // Optional: handle failure silently or log it
            }
    }

    fun Signup(email: String, password: String, name: String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            _authState.value = AuthState.Error("All fields are required")
            return
        }

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        saveUserName(userId, name)
                        _authState.value = AuthState.Authenticated
                    } else {
                        _authState.value = AuthState.Error("Sign-up failed: User ID is null.")
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something Went Wrong")
                }
            }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    // --- FIX: This function is now at the class level, not inside another function ---
    // It updates Firestore because that is where your app reads the name from.
    fun updateUserName(newName: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            // Update the 'name' field in the 'users' collection
            firestore.collection("users").document(userId)
                .update("name", newName)
                .addOnSuccessListener {
                    // Once updated in DB, refresh the local UI
                    getUserName()
                }
                .addOnFailureListener { e ->
                    _userDataState.value = UserDataState.Error("Failed to update name: ${e.message}")
                }
        }
    }

    fun getUserName() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _userDataState.value = UserDataState.Error("User not logged in.")
            return
        }

        _userDataState.value = UserDataState.Loading

        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name")
                    if (name != null) {
                        _userDataState.value = UserDataState.Success(name)
                    } else {
                        _userDataState.value = UserDataState.Error("User name field is missing in profile.")
                    }
                } else {
                    // Fallback: If no Firestore doc, try getting name from Auth object directly
                    val authName = auth.currentUser?.displayName
                    if (authName != null) {
                        _userDataState.value = UserDataState.Success(authName)
                    } else {
                        _userDataState.value = UserDataState.Error("User profile not found.")
                    }
                }
            }
            .addOnFailureListener { e ->
                _userDataState.value = UserDataState.Error("Failed to fetch user data: ${e.message}")
            }
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

sealed class UserDataState {
    object Idle : UserDataState()
    object Loading : UserDataState()
    data class Success(val name: String) : UserDataState()
    data class Error(val message: String) : UserDataState()
}