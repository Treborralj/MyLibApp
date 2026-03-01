package com.example.mylib.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mylib.data.repo.PostRepository
import com.example.mylib.data.repo.ReviewRepository
import com.example.mylib.data.repo.UserRepository
import com.example.mylib.viewModel.PostEditorViewModel
import com.example.mylib.viewModel.ProfileViewModel

class PostEditorViewModelFactory(
    private val postRepository: PostRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostEditorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostEditorViewModel(postRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}