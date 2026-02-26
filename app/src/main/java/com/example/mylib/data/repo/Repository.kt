package com.example.mylib.data.repo

import com.example.mylib.data.repo.Dao.UserDao
import kotlinx.coroutines.flow.Flow

class Repository(
    private val dao: UserDao
) {
    suspend fun addUser(name:String, id: Int, bio:String){
      dao.insert(User(name = name, id = id, bio = bio))

    }
    fun getUsers(): Flow<List<User>> {
        return dao.getAllUsers()
    }
}