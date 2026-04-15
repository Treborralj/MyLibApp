package com.example.mylib.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mylib.data.repo.ImageStorageManager
import com.example.mylib.data.repo.PostRepository
import com.example.mylib.data.repo.ReviewRepository
import com.example.mylib.data.repo.UserRepository
import com.example.mylib.viewModel.ProfileViewModel

class ProfileViewModelFactory(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val reviewRepository: ReviewRepository,
    private val storageManager: ImageStorageManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository,postRepository,reviewRepository, storageManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}