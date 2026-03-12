package com.example.mylib.data.repo

import com.example.mylib.data.remote.BookApi
import com.example.mylib.data.repo.Dao.BookDao
import com.example.mylib.data.repo.Dao.ReviewDao
import kotlinx.coroutines.flow.Flow

class BookRepository(
    private val api: BookApi,
    private val bookDao: BookDao,
    private val reviewDao: ReviewDao
) {

    fun observeBook(bookId: Int): Flow<Book?> {
        return bookDao.observeBookById(bookId)
    }

    suspend fun refreshBook(bookId: Int) {
        val remoteBook = api.searchBooks(id = bookId).firstOrNull()
        remoteBook?.let {
            val id = it.id ?: return@let
            bookDao.insert(
                Book(
                    id = id,
                    name = it.name ?: "",
                    genre = it.genre ?: "",
                    isbn = it.isbn ?: "",
                    writer = it.writer ?: "",
                    score = it.score ?: 0.0
                )
            )
        }
    }

    suspend fun updateLocalBookScore(bookId: Int) {
        val averageScore = reviewDao.getAverageScoreForBook(bookId)
        if (averageScore != null) {
            bookDao.updateBookScore(bookId, averageScore)
        }
    }
}
