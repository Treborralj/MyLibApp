package com.example.mylib.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mylib.data.repo.BookRepository
import com.example.mylib.data.repo.ReviewRepository
import com.example.mylib.viewModel.BookViewModel

class BookViewModelFactory(
    private val repository: BookRepository,
    private val reviewRepository: ReviewRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(repository,reviewRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
