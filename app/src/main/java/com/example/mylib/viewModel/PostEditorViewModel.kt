package com.example.mylib.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.MainActivity
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.SignupResponse
import com.example.mylib.data.repo.AuthenticationRepository
import com.example.mylib.data.repo.PostRepository
import com.example.mylib.viewModel.authentication.AuthenticationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class PostUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val result: PostResponse? = null
)


class PostEditorViewModel(
    private val repository: PostRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(PostUiState())
    val uiState = _uiState.asStateFlow()


    fun editPost(text: String, id:Int?){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null,
            )
            var response: PostResponse? = null
            try{
                if(id == null) {
                    response = repository.createPost(text)
                }
                else {
                    response = repository.editPost(text,id)
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
                error = null,
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