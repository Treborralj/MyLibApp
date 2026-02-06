package com.example.mylib.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mylib.data.repo.AuthenticationRepository

class AuthenticationViewModelFactory(
    private val repository: AuthenticationRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthenticationViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AuthenticationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}