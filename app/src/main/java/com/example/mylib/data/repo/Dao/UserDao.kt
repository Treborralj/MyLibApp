package com.example.mylib.data.repo.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mylib.data.repo.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Query("UPDATE User SET bio = :bio WHERE name = :name")
    suspend fun updateBio(name: String, bio: String)

    @Query("UPDATE User SET name = :newName WHERE name = :oldName")
    suspend fun updateName(oldName: String, newName: String)

    @Query("UPDATE User SET imagePath = :imagePath WHERE name = :name")
    suspend fun updateImage(name: String, imagePath: String?)
}