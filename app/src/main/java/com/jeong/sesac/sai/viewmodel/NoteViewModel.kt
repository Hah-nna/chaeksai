package com.jeong.sesac.sai.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.data.repository.NoteRepositoryImpl
import com.jeong.sesac.feature.model.Note
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteViewModel(
    private val noteRepo: NoteRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Boolean?>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    /**
     *  선택된 노트의 상태
     */
    private var _selectedNoteState = MutableStateFlow<UiState<NoteWithUser>>(UiState.Loading)
    val selectedNoteState = _selectedNoteState.asStateFlow()

    /**
     * 쪽지 update, delete 상태
     * */
    private var _fetchNoteState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val fetchNoteState = _fetchNoteState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNote(imgUri: String, title: String, content: String, libraryName: String, nickname: String) {
        val note = Note(
            id = "",
            userId = "",
            image = imgUri,
            title = title,
            content = content,
            libraryName = libraryName,
            likes = 0,
            createdAt = System.currentTimeMillis(),
        )

        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val isSuccess = noteRepo.createNote(note, nickname)
            _uiState.value =
                if (isSuccess) UiState.Success(isSuccess) else UiState.Error("다시 시도해주세요")

        }
    }

    /**
     * 노트 선택
     */
    fun selectNote(noteId: String) = viewModelScope.launch {
        _selectedNoteState.value = UiState.Loading
        noteRepo.getNote(noteId)
            .onSuccess { note ->
                _selectedNoteState.value = UiState.Success(note)
            }.onFailure { error ->
                _selectedNoteState.value = UiState.Error("노트를 불러오는데 실패했습니다")
            }
    }

    /**
     * 쪽지 업데이트
     * */
    fun updateNote(noteId: String, note: Note) = viewModelScope.launch {

        _fetchNoteState.value = UiState.Loading

        noteRepo.updateNote(noteId, note)
            .onSuccess {
                _fetchNoteState.value = UiState.Success(Unit)
            }.onFailure {
                _fetchNoteState.value = UiState.Error("쪽지 업데이트 실패")
            }
    }

    fun deleteNote(noteId: String) = viewModelScope.launch {
        _fetchNoteState.value = UiState.Loading
        noteRepo.deleteNote(noteId)
            .onSuccess {
                _fetchNoteState.value = UiState.Success(Unit)
            }.onSuccess {
                _fetchNoteState.value = UiState.Error("쪽지 삭제 실패")
            }

    }
}