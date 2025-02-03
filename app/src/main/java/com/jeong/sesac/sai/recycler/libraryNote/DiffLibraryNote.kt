package com.jeong.sesac.sai.recycler.libraryNote

import androidx.recyclerview.widget.DiffUtil
import com.jeong.sesac.feature.model.NoteWithUser

class DiffLibraryNote : DiffUtil.ItemCallback<NoteWithUser>() {

    override fun areItemsTheSame(oldItem: NoteWithUser, newItem: NoteWithUser): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: NoteWithUser, newItem: NoteWithUser): Boolean {
        return oldItem.id == newItem.id
    }

}