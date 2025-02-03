package com.jeong.sesac.sai.recycler.weeklyNote

import androidx.recyclerview.widget.DiffUtil
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.model.LibraryNoteUI

class DiffUtilWeeklyNotes : DiffUtil.ItemCallback<NoteWithUser>(){

    override fun areItemsTheSame(oldItem: NoteWithUser, newItem: NoteWithUser): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NoteWithUser, newItem: NoteWithUser): Boolean {
        return oldItem == newItem
    }
}