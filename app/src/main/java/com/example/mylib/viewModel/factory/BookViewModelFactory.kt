package com.example.mylib.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mylib.data.repo.ListRepository
import com.example.mylib.data.repo.BookRepository
import com.example.mylib.data.repo.ReviewRepository
import com.example.mylib.data.repo.ImageStorageManager
import com.example.mylib.data.repo.UserRepository
import com.example.mylib.viewModel.BookViewModel

class BookViewModelFactory(
    private val bookRepository: BookRepository,
    private val reviewRepository: ReviewRepository,
    private val listRepository: ListRepository,
    private val userRepository: UserRepository,
    private val storageManager: ImageStorageManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(bookRepository, reviewRepository, listRepository, userRepository, storageManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
