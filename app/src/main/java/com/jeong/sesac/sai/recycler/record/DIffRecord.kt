package com.jeong.sesac.sai.recycler.record

import androidx.recyclerview.widget.DiffUtil
import com.jeong.sesac.feature.model.NoteWithUser

class DIffRecord : DiffUtil.ItemCallback<NoteWithUser>() {

    override fun areContentsTheSame(oldItem: NoteWithUser, newItem: NoteWithUser): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(oldItem: NoteWithUser, newItem: NoteWithUser): Boolean {
        return oldItem == newItem
    }
}