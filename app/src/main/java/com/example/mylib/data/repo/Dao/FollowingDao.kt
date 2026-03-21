package com.example.mylib.data.repo.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mylib.data.repo.Following
import kotlinx.coroutines.flow.Flow
@Dao
interface FollowingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(following: Following)

    @Delete fun delete(following: Following)
    @Query("SELECT * FROM Following WHERE followingUsername = :username")
    fun getFollowedUsers(username: String): Flow<List<Following>>

    @Query("SELECT * FROM Following WHERE followedUsername = :username")
    fun getFollowingUsers(username: String): Flow<List<Following>>
    suspend fun clearAndInsert(username: String, followedUsers: List<String>) {
        followedUsers.forEach { followedUsername ->
            insert(Following(username, followedUsername))
        }}
}