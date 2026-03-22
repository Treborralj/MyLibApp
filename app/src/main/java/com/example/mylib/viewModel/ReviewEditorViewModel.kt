package com.example.mylib.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.repo.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReviewUiState(
    val loading: Boolean = false,
    val error: String = "",
    val result: ReviewResponse? = null
)


class ReviewEditorViewModel(
    private val repository: ReviewRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState = _uiState.asStateFlow()

    fun editReview(text: String, id: Int? = null, score: Double, bookId: Int?){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = "",
                result = null,
            )
            var response: ReviewResponse? = null
            try{
                if(id != null) {
                    response = repository.editReview(text,id, score)
                }
                else if (bookId != null) {
                    response = repository.createReview(text, bookId, score)
                }
                else {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = "Missing input"
                    )
                    return@launch
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

    fun deleteReview(id:Int){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = "",
            )
            try{

                repository.deleteReview(id)

                _uiState.value = _uiState.value.copy(
                    loading = false,
                )


            } catch (e: Exception){
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Couldn't delete review"
                )
            }
        }
    }


}