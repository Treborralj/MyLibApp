package com.example.mylib.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.data.repo.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EditUserUiState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class EditUserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditUserUiState())
    val uiState: StateFlow<EditUserUiState> = _uiState.asStateFlow()

    fun updateAccount(username: String?, bio: String?) {
        viewModelScope.launch {
            try {
                _uiState.value = EditUserUiState(isLoading = true)
                userRepository.updateAccount(username, bio)
                _uiState.value = EditUserUiState(successMessage = "Profile updated successfully")
            } catch (e: Exception) {
                _uiState.value = EditUserUiState(
                    errorMessage = e.message ?: "Failed to update profile"
                )
            }
        }
    }

    fun updatePassword(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = EditUserUiState(isLoading = true)
                userRepository.updatePassword(oldPassword, newPassword, confirmPassword)
                _uiState.value = EditUserUiState(successMessage = "Password updated successfully")
            } catch (e: Exception) {
                _uiState.value = EditUserUiState(
                    errorMessage = e.message ?: "Failed to update password"
                )
            }
        }
    }
}