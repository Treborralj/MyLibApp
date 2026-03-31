package com.example.mylib.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.repo.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class PostUiState(
    val loading: Boolean = false,
    val error: String = "",
    val result: PostResponse? = null
)

class PostEditorViewModel(
    private val repository: PostRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(PostUiState())
    val uiState = _uiState.asStateFlow()

    fun editPost(
        title: String,
        text: String,
        imageUri: Uri?,
        id: Int? = null
    ){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                result = null,
                loading = true,
                error = "",
            )
            var response: PostResponse? = null
            try{
                if(id == null) {
                    response = repository.createPost(title, text, imageUri)
                }
                else {
                    response = repository.editPost(id, title, text, imageUri)
                }
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    result = response
                )
            } catch (e: Exception){
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Couldn't save changes"
                )
            }
        }
    }

    fun deletePost(id:Int){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = "",
            )
            try{

                repository.deletePost(id)

                _uiState.value = _uiState.value.copy(
                    loading = false,
                )
            } catch (e: Exception){
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Couldn't delete post"
                )
            }
        }
    }
}