package com.example.mylib.data.repo.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mylib.data.repo.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("DELETE FROM Post WHERE id = :id")
    suspend fun deletePost(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post)

    @Query("UPDATE Post SET text = :text WHERE id = :id")
    suspend fun updatePost(id: Int, text: String)

    @Query("SELECT * FROM Post WHERE id= :id")
    suspend fun observePostById(id: Int): Post

    @Query("SELECT * FROM Post WHERE accountId = :accountId")
    fun observePostsByAccountId(accountId: Int): Flow<List<Post>>

}
