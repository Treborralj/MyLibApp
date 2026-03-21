package com.example.mylib.data.repo

import com.example.mylib.data.models.BookRequest
import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.remote.ListApi
import com.example.mylib.data.repo.Dao.BookListCrossRefDao
import com.example.mylib.data.repo.Dao.BookListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ListRepository(
    private val api: ListApi,
    private val dao: BookListDao,
    private val crossRef: BookListCrossRefDao
) {
    suspend fun getWantToRead(user: String): List<BookResponse> = withContext(Dispatchers.IO) {
        val result = api.getWantToRead()
        val listId = dao.getBookListId(user, "wantToRead")
        crossRef.insertAll(listId,result)
        crossRef.getListOfBooks(listId)
    }

    suspend fun addBookToWantToRead(user: String, book: BookRequest) = withContext(Dispatchers.IO) {
        api.addBookToWantToRead(book)
        val listId = dao.getBookListId(user, "wantToRead")
        crossRef.insert(
            BookListCrossRef(listId, book.id)
        )
    }

    suspend fun removeBookFromWantToRead(user: String, bookId: Int) = withContext(Dispatchers.IO) {
        api.removeBookFromWantToRead(bookId)
        val listId = dao.getBookListId(user, "wantToRead")
        crossRef.removeBookFromList(listId, bookId)
    }


    suspend fun getAmReading(user: String): List<BookResponse> = withContext(Dispatchers.IO) {
        val result = api.getAmReading()
        val listId = dao.getBookListId(user, "amReading")
        crossRef.insertAll(listId, result)
        crossRef.getListOfBooks(listId)
    }

    suspend fun addBookToAmReading(user: String, book: BookRequest) = withContext(Dispatchers.IO) {
        api.addBookToAmReading(book)
        val listId = dao.getBookListId(user, "amReading")
        crossRef.insert(
            BookListCrossRef(listId, book.id)
        )
    }

    suspend fun removeBookFromAmReading(user: String, bookId: Int) = withContext(Dispatchers.IO) {
        api.removeBookFromAmReading(bookId)
        val listId = dao.getBookListId(user, "amReading")
        crossRef.removeBookFromList(listId, bookId)
    }

    suspend fun getHaveRead(user: String): List<BookResponse> = withContext(Dispatchers.IO) {
        val result = api.getHaveRead()
        val listId = dao.getBookListId(user, "haveRead")
        crossRef.insertAll(listId, result)
        crossRef.getListOfBooks(listId)
    }

    suspend fun addBookToHaveRead(user: String, book: BookRequest) = withContext(Dispatchers.IO) {
        api.addBookToHaveRead(book)
        val listId = dao.getBookListId(user, "haveRead")
        crossRef.insert(
            BookListCrossRef(listId, book.id)
        )
    }

    suspend fun removeBookFromHaveRead(user: String, bookId: Int) = withContext(Dispatchers.IO) {
        api.removeBookFromHaveRead(bookId)
        val listId = dao.getBookListId(user, "haveRead")
        crossRef.removeBookFromList(listId, bookId)
    }
}
