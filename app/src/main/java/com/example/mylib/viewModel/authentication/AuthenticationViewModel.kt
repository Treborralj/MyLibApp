package com.example.mylib.viewModel.authentication

import androidx.lifecycle.ViewModel
import com.example.mylib.data.repo.AuthenticationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.mylib.MainActivity


class AuthenticationViewModel(
    private val repository: AuthenticationRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AuthenticationUiState())
    val uiState = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null,
                signupSuccess = null
            )
            try {
                val response = repository.login(username, password)
                MainActivity.bearerToken = response.token
                println("token: "+ MainActivity.bearerToken)
                MainActivity.loggedInUser = username
                println("user: " + MainActivity.loggedInUser)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    token = response.token
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Login failed"
                )
            }
        }
    }

    fun signup(username: String, password: String){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null,
                signupSuccess = null
            )
            try{
                val response = repository.signup(username, password)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    signupSuccess = response
                )
            } catch (e: Exception){
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Signup failed"
                )
            }
        }
    }

    fun clearSignupSuccess(){
        _uiState.value = _uiState.value.copy(signupSuccess = null)
    }

    fun clearError(){
        _uiState.value = _uiState.value.copy(error = null)
    }
}