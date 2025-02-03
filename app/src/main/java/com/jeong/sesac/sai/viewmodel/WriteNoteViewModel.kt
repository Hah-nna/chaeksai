package com.jeong.sesac.sai.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.data.repository.WriteNoteRepositoryImpl
import com.jeong.sesac.feature.model.Note
import com.jeong.sesac.sai.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class WriteNoteViewModel(
    private val writeNoteRepo: WriteNoteRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Boolean?>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

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
            createdAt = Date(),
        )

        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val isSuccess = writeNoteRepo.createNote(note, nickname)
            _uiState.value =
                if (isSuccess) UiState.Success(isSuccess) else UiState.Error("다시 시도해주세요")

        }
    }
}