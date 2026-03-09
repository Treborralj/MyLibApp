package com.example.mylib.data.repo

import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.remote.PostApi
import com.example.mylib.data.repo.Dao.PostDao

class PostRepository(
    private val api: PostApi,
    private val postDao: PostDao
) {

    suspend fun getAccountPosts(username: String): List<PostResponse> {
        return api.getAccountPosts(username)
    }

    suspend fun createPost(text: String): PostResponse {
        val hashMap = HashMap<String, Any>()
        hashMap["text"] = text
        val response = api.createPost(hashMap)
        
        postDao.insert(
            Post(
                id = response.id ?: 0,
                accountId = 0,
                text = response.text,
                time = response.time,
                //image = null,
                imageType = null
            )
        )
        return response
    }

    suspend fun editPost(text: String, id: Int): PostResponse {
        val hashMap = HashMap<String, Any>()
        hashMap["text"] = text
        hashMap["id"] = id
        val response = api.editPost(hashMap)
        postDao.updatePost(id, text)
        return response
    }

    suspend fun deletePost(id: Int) {
        postDao.deletePost(id)
        api.deletePost(id)
    }
}
