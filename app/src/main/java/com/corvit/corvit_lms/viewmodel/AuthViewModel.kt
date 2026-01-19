package com.corvit.corvit_lms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
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

    fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Check if user exists in Firestore, if not create them
                val userId = auth.currentUser?.uid
                val email = auth.currentUser?.email
                val name = auth.currentUser?.displayName ?: "Google User"

                if (userId != null) {
                    val userRef = firestore.collection("users").document(userId)
                    userRef.get().addOnSuccessListener { document ->
                        if (!document.exists()) {
                            saveUserName(userId, name)
                        }
                    }
                }

                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(task.exception?.message ?: "Error")
            }
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

    private fun saveUserName(userId: String, name: String) {
        val user = hashMapOf(
            "name" to name,
            "email" to auth.currentUser?.email
        )

        firestore.collection("users").document(userId)
            .set(user)
            .addOnFailureListener {
                // Handle failure silently or log it
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
        _userDataState.value = UserDataState.Idle
    }

    fun updateUserName(newName: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId)
                .update("name", newName)
                .addOnSuccessListener {
                    getUserData() // Refresh data
                }
                .addOnFailureListener { e ->
                    _userDataState.value = UserDataState.Error("Failed to update name: ${e.message}")
                }
        }
    }

    fun getUserData() {
        val userId = auth.currentUser?.uid
        val currentUser = auth.currentUser

        if (userId == null || currentUser == null) {
            _userDataState.value = UserDataState.Error("User not logged in.")
            return
        }

        _userDataState.value = UserDataState.Loading

        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name") ?: currentUser.displayName ?: "User"
                    val email = document.getString("email") ?: currentUser.email ?: ""

                    _userDataState.value = UserDataState.Success(name, email)
                } else {
                    // Fallback to Auth data if Firestore doc doesn't exist
                    val name = currentUser.displayName ?: "User"
                    val email = currentUser.email ?: ""
                    _userDataState.value = UserDataState.Success(name, email)
                }
            }
            .addOnFailureListener { e ->
                _userDataState.value = UserDataState.Error("Failed to fetch user data: ${e.message}")
            }
    }

    fun getUserName() {
        getUserData()
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
    data class Success(val name: String, val email: String) : UserDataState()
    data class Error(val message: String) : UserDataState()
}