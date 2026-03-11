package com.example.mylib.data.repo.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mylib.data.repo.BookList

@Dao
interface BookListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // need to connect to login and account creation
    suspend fun insert(bookList: BookList)

    @Query("SELECT listId FROM BookList WHERE accountId = :userId AND type = :name")
    suspend fun getBookListId(userId: Int, name: String): Int

    @Delete
    suspend fun delete(bookList: BookList)
}