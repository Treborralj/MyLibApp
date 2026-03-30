package com.example.mylib.data.repo

import com.example.mylib.data.models.PostCreateRequest
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.PostUpdateRequest
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
        val request = PostCreateRequest(text = text)
        val response = api.createPost(request)
        
        postDao.insert(
            Post(
                id = response.id ?: 0,
                accountId = 0,
                text = response.text,
                time = response.time,
                imagePath = null
            )
        )
        return response
    }

    suspend fun editPost(text: String, id: Int): PostResponse {
        val request = PostUpdateRequest(id = id, text = text)
        val response = api.editPost(request)
        postDao.updatePost(id, text)
        return response
    }

    suspend fun deletePost(id: Int) {
        postDao.deletePost(id)
        api.deletePost(id)
    }
}
