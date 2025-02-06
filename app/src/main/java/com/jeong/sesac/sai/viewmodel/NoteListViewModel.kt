package com.jeong.sesac.sai.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.data.repository.NoteListRepositoryImpl
import com.jeong.sesac.domain.model.NoteFilterType
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.model.LibraryNoteUI
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.model.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


// presentation 레이어에 위치
object NoteMapper {
    fun NoteWithUser.toLibraryNoteUI(): LibraryNoteUI {
        return LibraryNoteUI(
            id = this.id,
            userInfo = UserInfo(
                id = this.userInfo.id,
                profile = this.userInfo.profile,
                nickName = this.userInfo.nickName
            ),
            image = if (this.image.isNotEmpty()) this.image else "",
            title = this.title,
            createdAt = System.currentTimeMillis(),
            libraryName = this.libraryName,
            content = this.content,
            likes = this.likes,
        )
    }
}

class NoteListViewModel(private val noteListRepo: NoteListRepositoryImpl) : ViewModel() {
    // 현재 표시 중인 노트 리스트의 상태
    private var _noteListState = MutableStateFlow<UiState<List<NoteWithUser>>>(UiState.Loading)
    val noteListState = _noteListState.asStateFlow()

    // 선택된 노트의 상태
    private var _selectedNoteState = MutableStateFlow<UiState<NoteWithUser?>>(UiState.Loading)
    val selectedNoteState = _selectedNoteState.asStateFlow()

    // 노트 리스트를 가져오는 함수
    fun getNoteList(filterType: NoteFilterType, nickname: String? = null) = viewModelScope.launch {
        _noteListState.value = UiState.Loading

        noteListRepo.getNoteList(filterType, nickname)
            .onSuccess { notes ->
                _noteListState.value = UiState.Success(notes)
            }.onFailure { error ->
                _noteListState.value = UiState.Error("노트 목록을 불러오는데 실패했습니다")
            }
    }

    // 노트 선택 함수
    fun selectNote(noteId: String) = viewModelScope.launch {
        _selectedNoteState.value = UiState.Loading

        // 쪽지리스트(_noteListState)에서 클릭한 노트 찾기 시도
        val note = (_noteListState.value as? UiState.Success)?.data?.find { it.id == noteId }

        if (note != null) {
            _selectedNoteState.value = UiState.Success(note)
        } else {
            // 쪽지를 찾지 못했으면 쪽지리스트를 다시 로드
            getNoteList(NoteFilterType.ByCreatedAt(false))
            _noteListState.collectLatest { state ->
                if (state is UiState.Success) {
                    state.data.find { it.id == noteId }?.let {
                        _selectedNoteState.value = UiState.Success(it)
                    } ?: run {
                        _selectedNoteState.value = UiState.Error("다시 시도해주세요")
                    }
                }
            }
        }
    }
}