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
    private var _commentState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val commentState = _commentState.asStateFlow()

    private var _commentListState =
        MutableStateFlow<UiState<List<CommentWithUser>>>(UiState.Loading)
    val commentListState = _commentListState.asStateFlow()

    private var _commentUpdateState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val commentUpdateState = _commentUpdateState.asStateFlow()

    private var _commentDeleteState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val commentDeleteState = _commentDeleteState.asStateFlow()

    fun createComment(nickname: String, noteId: String, comment: Comment) {
        viewModelScope.launch {
            _commentState.value = UiState.Loading

            val isSuccess = commentRepo.createComment(nickname, noteId, comment)
            _commentState.value =
                if (isSuccess) {
                    getComments(nickname, noteId)
                    UiState.Success(true)
                } else UiState.Error("다시 시도해주세요")
        }
    }

    fun getComments(nickname: String, noteId: String) {
        viewModelScope.launch {
            _commentListState.value = UiState.Loading


            viewModelScope.launch {
                _commentListState.value = UiState.Loading

                commentRepo.getComments(nickname, noteId)
                    .onSuccess {
                        _commentListState.value = UiState.Success(it.reversed())
                    }.onFailure {
                        _commentListState.value = UiState.Error("다시 시도해주세요")
                    }
            }

        }
    }


    fun updateComment(nickname: String, noteId: String, commentId: String, content: String) {
        viewModelScope.launch {
            _commentUpdateState.value = UiState.Loading

            commentRepo.updateComment(noteId, commentId, content)
                .onSuccess {
                    _commentUpdateState.value = UiState.Success(Unit)
                    getComments(nickname, noteId)
                }.onFailure {
                    _commentUpdateState.value = UiState.Error("댓글 수정에 실패했습니다")
                }
        }
    }

    fun deleteComment(nickname: String, noteId: String, commentId: String) {
        viewModelScope.launch {
            _commentDeleteState.value = UiState.Loading
            commentRepo.deleteComment(noteId, commentId)
                .onSuccess {
                    _commentDeleteState.value = UiState.Success(Unit)
                    getComments(nickname, noteId)
                }.onFailure {
                    _commentDeleteState.value = UiState.Error("댓글 삭제에 실패했습니다\n다시시도해주세요")
                }
        }
    }
}