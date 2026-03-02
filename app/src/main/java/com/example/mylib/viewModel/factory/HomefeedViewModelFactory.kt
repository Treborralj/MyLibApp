package com.example.mylib.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mylib.data.repo.ReviewRepository
import com.example.mylib.data.repo.UserRepository
import com.example.mylib.viewModel.BookViewModel
import com.example.mylib.viewModel.HomefeedViewModel

class HomefeedViewModelFactory(
    private val repository: UserRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomefeedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomefeedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}