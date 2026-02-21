package com.corvit.corvit_lms.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            
            val userId = auth.currentUser?.uid
            val name = auth.currentUser?.displayName ?: "Google User"
            
            if (userId != null) {
                val userRef = firestore.collection("users").document(userId)
                val document = userRef.get().await()
                if (!document.exists()) {
                    saveUserName(userId, name)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signup(email: String, password: String, name: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val userId = auth.currentUser?.uid
            if (userId != null) {
                saveUserName(userId, name)
                Result.success(Unit)
            } else {
                Result.failure(Exception("User ID is null after sign up."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveUserName(userId: String, name: String) {
        val userMap = hashMapOf(
            "name" to name,
            "email" to auth.currentUser?.email
        )
        try {
            firestore.collection("users").document(userId).set(userMap).await()
        } catch (e: Exception) {
            // Log failure or handle accordingly
        }
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun updateUserName(newName: String): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
            firestore.collection("users").document(userId).update("name", newName).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserData(): Result<Pair<String, String>> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
            val currentUser = auth.currentUser!!

            val document = firestore.collection("users").document(userId).get().await()
            if (document.exists()) {
                val name = document.getString("name") ?: currentUser.displayName ?: "User"
                val email = document.getString("email") ?: currentUser.email ?: ""
                Result.success(Pair(name, email))
            } else {
                val name = currentUser.displayName ?: "User"
                val email = currentUser.email ?: ""
                Result.success(Pair(name, email))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
