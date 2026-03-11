package com.example.mylib.data.repo.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.repo.Book
import com.example.mylib.data.repo.BookListCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface BookListCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crossRef: BookListCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listId: Int, books: List<BookResponse>) {
        books.forEach { book ->
            insert(BookListCrossRef(listId, book.id))
        }
    }

    @Query("SELECT * FROM book WHERE id IN (SELECT bookId FROM BookListCrossRef WHERE listId = :listId)")
    fun getListOfBooks(listId: Int): List<BookResponse>

    @Query ("DELETE FROM BookListCrossRef WHERE listId = :listId")
    suspend fun removeList(listId: Int)

    @Query("DELETE FROM BookListCrossRef WHERE listId = :listId AND bookId = :bookId")
    suspend fun removeBookFromList(listId: Int, bookId: Int)



}