package com.example.mylib.data.repo.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.mylib.data.repo.Following
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(following: Following)

    @Delete
    suspend fun delete(following: Following)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(followings: List<Following>)

    @Query("DELETE FROM Following WHERE followingUsername = :username")
    suspend fun clearForUser(username: String)

    @Query("SELECT * FROM Following WHERE followingUsername = :username")
    fun getFollowedUsers(username: String): Flow<List<Following>>

    @Query("SELECT * FROM Following WHERE followedUsername = :username")
    fun getFollowingUsers(username: String): Flow<List<Following>>

    @Transaction
    suspend fun clearAndInsert(username: String, followedUsers: List<Following>) {
        clearForUser(username)
        insertAll(followedUsers)
    }
}