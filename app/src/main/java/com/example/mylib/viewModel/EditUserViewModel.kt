package com.example.mylib.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.MainActivity
import com.example.mylib.data.repo.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

data class EditUserUiState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val requiresRelogin: Boolean = false
)

class EditUserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditUserUiState())
    val uiState: StateFlow<EditUserUiState> = _uiState.asStateFlow()

    fun updateAccount(currentUsername: String, username: String?, bio: String?) {
        viewModelScope.launch {
            try {
                _uiState.value = EditUserUiState(isLoading = true)

                val trimmedUsername = username?.trim()
                val trimmedBio = bio?.trim()

                val usernameChanged =
                    !trimmedUsername.isNullOrBlank() &&
                            trimmedUsername != currentUsername.trim()

                userRepository.updateAccount(
                    username = trimmedUsername.takeIf { !it.isNullOrBlank() },
                    bio = trimmedBio.takeIf { !it.isNullOrBlank() }
                )

                if (usernameChanged) {
                    MainActivity.loggedInUser = trimmedUsername!!
                }

                _uiState.value = EditUserUiState(
                    successMessage = if (usernameChanged) {
                        "Username changed successfully. Please log in again."
                    } else {
                        "Profile updated successfully."
                    },
                    requiresRelogin = usernameChanged
                )
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

                _uiState.value = EditUserUiState(
                    successMessage = "Password updated successfully. Please log in again.",
                    requiresRelogin = true
                )
            } catch (e: Exception) {
                _uiState.value = EditUserUiState(
                    errorMessage = e.message ?: "Failed to update password"
                )
            }
        }
    }

    fun updateProfilePicture(file: File) {
        viewModelScope.launch {
            try {
                _uiState.value = EditUserUiState(isLoading = true)

                userRepository.updateProfilePicture(file)

                _uiState.value = EditUserUiState(
                    successMessage = "Profile picture updated successfully."
                )
            } catch (e: Exception) {
                _uiState.value = EditUserUiState(
                    errorMessage = e.message ?: "Failed to update profile picture"
                )
            }
        }
    }

    fun clearState() {
        _uiState.value = EditUserUiState()
    }
}