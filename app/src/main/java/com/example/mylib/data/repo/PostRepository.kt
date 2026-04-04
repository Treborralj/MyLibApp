package com.example.mylib.data.repo

import android.content.Context
import android.net.Uri
import androidx.test.espresso.base.Default

import com.example.mylib.data.models.PostCreateRequest
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.PostUpdateRequest
import com.example.mylib.data.remote.PostApi
import com.example.mylib.ui.util.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

import com.example.mylib.data.repo.Dao.PostDao
import com.example.mylib.viewModel.PostReviewItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.io.encoding.Base64


class PostRepository(
    private val api: PostApi,
    private val context: Context,
    private val postDao: PostDao,
    private val imageStorage: ImageStorageManager
) {

    suspend fun getAccountPosts(username: String): List<PostResponse> {
        return api.getAccountPosts(username)
    }
    suspend fun createPost(
        title: String,
        text: String,
        imageUri: Uri?
    ): PostResponse {
        val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val textPart = text.toRequestBody("text/plain".toMediaTypeOrNull())
        val filePart = imageUri?.let { buildImagePart(it) }
        return api.createPost(
            title = titlePart,
            text = textPart,
            file = filePart
        )
    }

    suspend fun editPost(
        id: Int,
        title: String,
        text: String,
        imageUri: Uri?
    ): PostResponse {
        val idPart = id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val textPart = text.toRequestBody("text/plain".toMediaTypeOrNull())
        val filePart = imageUri?.let { buildImagePart(it) }
        return api.editPost(
            id = idPart,
            title = titlePart,
            text = textPart,
            file = filePart
        )
    }

    suspend fun deletePost(id: Int) {
        return api.deletePost(id)
    }

    private fun buildImagePart(uri: Uri): MultipartBody.Part{
        val file = uriToFile(context, uri)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestBody
        )
    }

    fun observePostsByUsername(username: String): Flow<List<PostReviewItem.PostItem>> {
        return postDao.observePostsByUsername(username)
            .map{ it.map{post ->
                PostReviewItem.PostItem(PostResponse(
                    id =post.id,
                    username =post.username,
                    title =post.title,
                    text =post.text,
                    time =post.time,
                    imageType = post.imageType,
                    imageBase64 = imageStorage.getFile(post.imagePath)?.readBytes()?.let { source -> Base64.encode(source) }
                ))} }
    }
}


