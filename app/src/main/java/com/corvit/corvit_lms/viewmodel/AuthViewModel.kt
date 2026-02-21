package com.corvit.corvit_lms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corvit.corvit_lms.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _userDataState = MutableLiveData<UserDataState>(UserDataState.Idle)
    val userDataState: LiveData<UserDataState> = _userDataState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        _authState.value = if (authRepository.currentUser == null) {
            AuthState.Unauthenticated
        } else {
            AuthState.Authenticated
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signInWithGoogle(idToken)
                .onSuccess {
                    _authState.value = AuthState.Authenticated
                }
                .onFailure { e ->
                    _authState.value = AuthState.Error(e.message ?: "Authentication failed")
                }
        }
    }

    fun Login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email & Password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            authRepository.login(email, password)
                .onSuccess {
                    _authState.value = AuthState.Authenticated
                }
                .onFailure { e ->
                    _authState.value = AuthState.Error(e.message ?: "Something Went Wrong")
                }
        }
    }



    fun Signup(email: String, password: String, name: String) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            _authState.value = AuthState.Error("All fields are required")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            authRepository.signup(email, password, name)
                .onSuccess {
                    _authState.value = AuthState.Authenticated
                }
                .onFailure { e ->
                    _authState.value = AuthState.Error(e.message ?: "Sign-up failed.")
                }
        }
    }

    fun logout() {
        authRepository.logout()
        _authState.value = AuthState.Unauthenticated
        _userDataState.value = UserDataState.Idle
    }

    fun updateUserName(newName: String) {
        viewModelScope.launch {
            authRepository.updateUserName(newName)
                .onSuccess {
                    getUserData() // Refresh data
                }
                .onFailure { e ->
                    _userDataState.value = UserDataState.Error("Failed to update name: ${e.message}")
                }
        }
    }

    fun getUserData() {
        _userDataState.value = UserDataState.Loading
        viewModelScope.launch {
            authRepository.getUserData()
                .onSuccess { (name, email) ->
                    _userDataState.value = UserDataState.Success(name, email)
                }
                .onFailure { e ->
                    _userDataState.value = UserDataState.Error("Failed to fetch user data: ${e.message}")
                }
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