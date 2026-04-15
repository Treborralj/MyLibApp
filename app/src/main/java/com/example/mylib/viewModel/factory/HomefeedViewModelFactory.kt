package com.example.mylib.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mylib.data.repo.ImageStorageManager
import com.example.mylib.data.repo.UserRepository
import com.example.mylib.viewModel.HomefeedViewModel

class HomefeedViewModelFactory(
    private val repository: UserRepository,
    private val storageManager: ImageStorageManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomefeedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomefeedViewModel(repository, storageManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}