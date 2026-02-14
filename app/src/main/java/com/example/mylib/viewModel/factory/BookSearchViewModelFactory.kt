package com.example.mylib.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mylib.data.repo.BookRepository
import com.example.mylib.viewModel.BookSearchViewModel


class BookSearchViewModelFactory (
    private val repository: BookRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(BookSearchViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return BookSearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

