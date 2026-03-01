package com.example.mylib.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mylib.data.repo.PostRepository
import com.example.mylib.data.repo.ReviewRepository
import com.example.mylib.viewModel.PostEditorViewModel
import com.example.mylib.viewModel.ReviewEditorViewModel

class ReviewEditorViewModelFactory(
    private val reviewRepository: ReviewRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewEditorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewEditorViewModel(reviewRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}