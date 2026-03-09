package com.example.mylib.data.repo.Dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface FollowingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(following: Following)

    @Delete fun delete(following: Following)


    @Query("SELECT followedId FROM Following WHERE followingId = :userId")
    suspend fun getFollowedUsers(userId: Int): List
}