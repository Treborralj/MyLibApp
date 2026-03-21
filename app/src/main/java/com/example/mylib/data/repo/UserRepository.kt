package com.example.mylib.data.repo

import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.remote.UserApi
import com.example.mylib.data.repo.Dao.PostDao
import com.example.mylib.data.repo.Dao.UserDao

class UserRepository(
    private val userApi: UserApi,
    private val postDao: PostDao,
    private val userDao: UserDao,
    private val imageStorage: ImageStorageManager
) {
    suspend fun fetchFeed(): List<PostResponse>{
        return userApi.fetchFeed()
    }

    suspend fun fetchAndStorePhoto(name:String){
        val response = userApi.fetchPhoto(name)

        val path = imageStorage.saveBase64Image(response.imageBase64, response.type, name)
        userDao.updateImage(name,path)

    }
}