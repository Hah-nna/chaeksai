package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.data.repository.CommentRepositoryImpl
import com.jeong.sesac.feature.model.Comment
import com.jeong.sesac.feature.model.CommentWithUser
import com.jeong.sesac.sai.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommentViewModel(private val commentRepo: CommentRepositoryImpl) : ViewModel() {
    private val _commentState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val commentState = _commentState.asStateFlow()

    private val _commentListState = MutableStateFlow<UiState<List<CommentWithUser>>>(UiState.Loading)
    val commentListState = _commentListState.asStateFlow()

     fun createComment(nickname: String, noteId: String, comment: Comment) {
        viewModelScope.launch {
            _commentState.value = UiState.Loading

            _commentState.value = UiState.Loading

            val isSuccess = commentRepo.createComment(nickname, noteId, comment)
            _commentState.value =
                if (isSuccess) {
                    getComments(nickname, noteId)
                    UiState.Success(true)
                } else UiState.Error("다시 시도해주세요")
        }
    }

     fun getComments(nickname: String, noteId: String)  {
        viewModelScope.launch {
            _commentListState.value = UiState.Loading

            val isSuccess = commentRepo.getComments(nickname, noteId)
            _commentListState.value = if (isSuccess.size > 0) UiState.Success(isSuccess.reversed()) else UiState.Error("다시 시도해주세요")
        }
    }
}