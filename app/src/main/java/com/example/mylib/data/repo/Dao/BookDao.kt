package com.example.mylib.data.repo.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mylib.data.repo.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: Book)

    @Query("SELECT * FROM book")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM book WHERE id = :id")
    fun observeBookById(id: Int): Flow<Book?>

    @Query("SELECT * FROM book " +
                    "WHERE (:term IS NULL " +
                    "OR isbn = :term " +
                    "OR genre = :term " +
                    "OR writer = :term " +
                    "OR name = :term )" +
                    "AND (:score IS NULL OR score >= :score)")
    fun getBookBySearch(term: String?, score: Double?): Flow<List<Book>>
}