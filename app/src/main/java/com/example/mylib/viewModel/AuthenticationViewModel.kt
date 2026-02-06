package com.example.mylib.viewModel

import androidx.lifecycle.ViewModel
import com.example.mylib.data.repo.AuthenticationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

data class AuthenticationUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val token: String? = null
)

class AuthenticationViewModel(
    private val repository: AuthenticationRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AuthenticationUiState())
    val uiState = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthenticationUiState(loading = true)
            try {
                val response = repository.login(username, password)
                _uiState.value = AuthenticationUiState(token = response.token)
            } catch (e: Exception) {
                _uiState.value = AuthenticationUiState(error = e.message ?: "Login failed")
            }
        }
    }
}