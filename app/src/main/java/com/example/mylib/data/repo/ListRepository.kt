package com.example.mylib.data.repo

import com.example.mylib.data.models.BookRequest
import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.remote.ListApi
import com.example.mylib.data.repo.Dao.BookListCrossRefDao
import com.example.mylib.data.repo.Dao.BookListDao

class ListRepository(
    private val api: ListApi,
    private val dao: BookListDao,
    private val crossRef: BookListCrossRefDao
    ) {
    suspend fun getWantToRead(userId: Int): List<BookResponse>{
        val result = api.getWantToRead()
        val listId = dao.getBookListId(userId, "wantToRead")
        crossRef.insertAll(listId,result)
        return crossRef.getListOfBooks(listId)
    }
    suspend fun addBookToWantToRead(userId: Int, book: BookRequest){
        api.addBookToWantToRead(book)
        val listId = dao.getBookListId(userId, "wantToRead")
        crossRef.insert(
            BookListCrossRef(listId, book.id)
        )
    }
    suspend fun removeBookFromWantToRead(userId: Int, bookId: Int){
        api.removeBookFromWantToRead(bookId)
        val listId = dao.getBookListId(userId, "wantToRead")
        crossRef.removeBookFromList(listId, bookId)
    }

    suspend fun getAmReading(userId: Int): List<BookResponse>{
        val result = api.getAmReading()
        val listId = dao.getBookListId(userId, "amReading")
        crossRef.insertAll(listId, result)
        return crossRef.getListOfBooks(listId)
    }
    suspend fun addBookToAmReading(userId: Int, book: BookRequest){
        api.addBookToAmReading(book)
        val listId = dao.getBookListId(userId, "amReading")
        crossRef.insert(
            BookListCrossRef(listId, book.id)
        )
    }


    suspend fun removeBookFromAmReading(userId: Int, bookId: Int){
        api.removeBookFromAmReading(bookId)
        val listId = dao.getBookListId(userId, "amReading")
        crossRef.removeBookFromList(listId, bookId)
    }

    suspend fun getHaveRead(userId: Int): List<BookResponse>{
        val result = api.getHaveRead()
        val listId = dao.getBookListId(userId, "haveRead")
        crossRef.insertAll(listId, result)
        return crossRef.getListOfBooks(listId)
    }
    suspend fun addBookToHaveRead(userId: Int, book: BookRequest){
        api.addBookToHaveRead(book)
        val listId = dao.getBookListId(userId, "haveRead")
        crossRef.insert(
            BookListCrossRef(listId, book.id)
        )
    }
    suspend fun removeBookFromHaveRead(userId: Int, bookId: Int){
        api.removeBookFromHaveRead(bookId)
        val listId = dao.getBookListId(userId, "haveRead")
        crossRef.removeBookFromList(listId, bookId)

    }
}