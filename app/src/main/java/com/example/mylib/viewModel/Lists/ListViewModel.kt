package com.example.mylib.viewModel.Lists

import com.example.mylib.data.repo.ListRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.MainActivity.Companion.loggedInUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListViewModel(private val repository: ListRepository): ViewModel(){
   private val _uiState = MutableStateFlow(ListUiState())
   val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()
    fun toggle(listType: ListType){
        _uiState.update { state ->
            val newSet =
                if(state.expanded.contains(listType)) {
                    state.expanded - listType
                }
            else{
                state.expanded + listType
                }
            state.copy(expanded = newSet)
        }
    }
    fun loadLists(){
        loadList(ListType.WANT_TO_READ)
        loadList(ListType.AM_READING)
        loadList(ListType.HAVE_READ)

    }

    private fun loadList(listType: ListType){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try{
                val books = when(listType){
                    ListType.WANT_TO_READ -> repository.getWantToRead(loggedInUser)
                    ListType.AM_READING -> repository.getAmReading(loggedInUser)
                    ListType.HAVE_READ -> repository.getHaveRead(loggedInUser)
                }
                _uiState.update { state ->
                    when(listType){
                        ListType.WANT_TO_READ -> state.copy(isLoading = false, wantToRead = books)
                        ListType.AM_READING -> state.copy(isLoading = false, amReading = books)
                        ListType.HAVE_READ -> state.copy(isLoading = false, haveRead = books)
                    }
                }
            }catch (e: Exception){
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun removeFromList(listType: ListType, bookId: Int){
        viewModelScope.launch {
            val beforeState = _uiState.value

            _uiState.value = when (listType){
                ListType.WANT_TO_READ -> beforeState.copy(
                    wantToRead = beforeState.wantToRead.filterNot{ it.id == bookId },
                    error = null
                )

                ListType.AM_READING -> beforeState.copy(
                    amReading = beforeState.amReading.filterNot { it.id == bookId },
                    error = null
                )

                ListType.HAVE_READ -> beforeState.copy(
                    haveRead = beforeState.haveRead.filterNot { it.id == bookId },
                    error = null
                )

            }
            try {
                when(listType){
                    ListType.WANT_TO_READ -> repository.removeBookFromWantToRead(loggedInUser, bookId)
                    ListType.AM_READING -> repository.removeBookFromAmReading(loggedInUser, bookId)
                    ListType.HAVE_READ -> repository.removeBookFromHaveRead(loggedInUser, bookId)
                }
            }catch(e: Exception){
                _uiState.value = beforeState.copy(
                    error = e.message
                )
            }
        }
    }
}