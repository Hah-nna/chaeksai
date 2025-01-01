package com.jeong.sesac.sai.viewmodel

import androidx.lifecycle.ViewModel
import com.jeong.sesac.sai.dto.Note
import com.jeong.sesac.sai.repository.WeeklyNotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WeeklyNotesViewModel(private val weeklyNoteRepo : WeeklyNotesRepository) : ViewModel() {
    private val _weeklyNotes = MutableStateFlow<List<Note>>(emptyList())
    val weeklyNotes get() = _weeklyNotes.asStateFlow()

    fun loadWeeklyNoteList() {
        // val weeklyNotess = weeklyNoteRepo.getWeeklyNotes()호출
        // 성공하면 _weeklyNotes = weeklyNotess
    }
}