package com.jeong.sesac.sai.recycler.newNote

import androidx.recyclerview.widget.DiffUtil
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.model.LibraryNoteUI

class DiffUtilNewNote : DiffUtil.ItemCallback<NoteWithUser>() {
    override fun areItemsTheSame(oldItem: NoteWithUser, newItem: NoteWithUser): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: NoteWithUser, newItem: NoteWithUser): Boolean {
        return oldItem.id == newItem.id
    }

}