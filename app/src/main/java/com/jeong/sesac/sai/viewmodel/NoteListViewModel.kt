package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.domain.model.NoteFilterType
import com.jeong.sesac.domain.model.SortOrder
import com.jeong.sesac.domain.repository.INoteListRepository
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class NoteListViewModel(private val noteListRepo: INoteListRepository) : ViewModel() {
    /**
     * 현재 표시 중인 노트 리스트의 상태
     */
    private var _noteListState = MutableStateFlow<UiState<List<NoteWithUser>>>(UiState.Loading)
    val noteListState = _noteListState.asStateFlow()

    // 이번주 인기 쪽지
    private var _weeklyNotesState = MutableStateFlow<UiState<List<NoteWithUser>>>(UiState.Loading)
    var weeklyNotesState = _weeklyNotesState.asStateFlow()

    // 최근 등록된 쪽지
    private var _newNotesState = MutableStateFlow<UiState<List<NoteWithUser>>>(UiState.Loading)
    var newNotesState = _newNotesState.asStateFlow()

    /**
     * 도서관별 노트 리스트 상태
     */
    private var _libraryNotesState = MutableStateFlow<UiState<List<NoteWithUser>>>(UiState.Loading)
    val libraryNotesState = _libraryNotesState.asStateFlow()

    // 포스트 좋아요 상태
    private var _likeState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    var likeState = _likeState.asStateFlow()


    /**
     * 노트 리스트를 가져오기(전체)
     */
    fun getNoteList(filterType: NoteFilterType, userId: String) = viewModelScope.launch {
        _noteListState.value = UiState.Loading

        noteListRepo.getNoteList(filterType, userId)
            .onSuccess { notes ->
                _noteListState.value = UiState.Success(notes)
            }.onFailure { error ->
                _noteListState.value = UiState.Error("노트 목록을 불러오는데 실패했습니다")
            }
    }

    fun getNewNoteList(userId: String) = viewModelScope.launch {
        _newNotesState.value = UiState.Loading

        noteListRepo.getNoteList(NoteFilterType.ThisWeek(SortOrder.LATEST), userId)
            .onSuccess { notes ->
                _newNotesState.value = UiState.Success(notes)
            }.onFailure { error ->
                _newNotesState.value = UiState.Error("인기 노트 목록을 불러오는데 실패했습니다")
            }
    }

    fun getWeeklyNoteList(userId: String) = viewModelScope.launch {
        _weeklyNotesState.value = UiState.Loading

        noteListRepo.getNoteList(NoteFilterType.ThisWeek(SortOrder.LIKES_DESC), userId)
            .onSuccess { notes ->
                _weeklyNotesState.value = UiState.Success(notes)
            }.onFailure { error ->
                _weeklyNotesState.value = UiState.Error("인기 노트 목록을 불러오는데 실패했습니다")
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

    fun toggleLikes(noteId: String, userId: String) = viewModelScope.launch {
        _likeState.value = UiState.Loading
        noteListRepo.toggleLike(noteId, userId)
            .onSuccess {
                _likeState.value = UiState.Success(it)

            }.onFailure { error ->
                _likeState.value = UiState.Error("좋아요에 실패했습니다\n다시시도해주세요")
            }
     }
}