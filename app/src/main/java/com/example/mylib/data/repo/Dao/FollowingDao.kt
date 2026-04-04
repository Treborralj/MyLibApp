package com.example.mylib.data.repo.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.mylib.data.repo.Following
import com.example.mylib.data.repo.Post
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
    suspend fun clearFollowingForUser(username: String)

    @Query("DELETE FROM Following WHERE followedUsername = :username")
    suspend fun clearFollowersForUser(username: String)

    @Query("SELECT * FROM Following WHERE followingUsername = :username")
    fun getFollowedUsers(username: String): List<Following>

    @Query("SELECT * FROM Following WHERE followedUsername = :username")
    fun getFollowingUsers(username: String): List<Following>

    @Transaction
    suspend fun clearFollowingAndInsert(username: String, followedUsers: List<Following>) {
        clearFollowingForUser(username)
        insertAll(followedUsers)
    }

    @Transaction
    suspend fun clearFollowersAndInsert(username: String, followers: List<Following>) {
        clearFollowersForUser(username)
        insertAll(followers)
    }

    @Query("SELECT * FROM `Following` WHERE followedUsername = :username")
    fun observeFollowersByUsername(username: String): Flow<List<Following>>

    @Query("SELECT * FROM `Following` WHERE followingUsername = :username")
    fun observeFollowingByUsername(username: String): Flow<List<Following>>
}
