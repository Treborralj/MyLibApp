package com.example.mylib.viewModel.authentication

import com.example.mylib.data.models.SignupResponse

data class AuthenticationUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val token: String? = null,
    val signupSuccess: SignupResponse? = null
)