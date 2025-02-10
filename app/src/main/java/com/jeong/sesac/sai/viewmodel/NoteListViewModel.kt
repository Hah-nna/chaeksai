package com.jeong.sesac.sai.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.data.repository.NoteListRepositoryImpl
import com.jeong.sesac.domain.model.NoteFilterType
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class NoteListViewModel(private val noteListRepo: NoteListRepositoryImpl) : ViewModel() {
    /**
     * 현재 표시 중인 노트 리스트의 상태
     */
    private var _noteListState = MutableStateFlow<UiState<List<NoteWithUser>>>(UiState.Loading)
    val noteListState = _noteListState.asStateFlow()

    /**
     * 도서관별 노트 리스트 상태
     */
    private var _libraryNotesState = MutableStateFlow<UiState<List<NoteWithUser>>>(UiState.Loading)
    val libraryNotesState = _libraryNotesState.asStateFlow()

    /**
     * 노트 리스트를 가져오기(전체)
     */
    fun getNoteList(filterType: NoteFilterType, nickname: String? = null) = viewModelScope.launch {
        _noteListState.value = UiState.Loading

        noteListRepo.getNoteList(filterType, nickname)
            .onSuccess { notes ->
                _noteListState.value = UiState.Success(notes)
            }.onFailure { error ->
                _noteListState.value = UiState.Error("노트 목록을 불러오는데 실패했습니다")
            }
    }

    /**
     * 도서관별 쪽지 리스트 가져오기
     * */
    fun getLibraryNotes(libraryName: String) = viewModelScope.launch {
        _libraryNotesState.value = UiState.Loading
        noteListRepo.getLibraryNotes(libraryName)
            .onSuccess { note ->
                _libraryNotesState.value = UiState.Success(note)
            }.onFailure { error ->
                _libraryNotesState.value = UiState.Error("노트를 불러오는데 실패했습니다")
            }
    }
}