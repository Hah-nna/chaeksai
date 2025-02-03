package com.jeong.sesac.sai.recycler.weeklyNote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import coil3.load
import coil3.request.crossfade
import coil3.size.Scale
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.databinding.ItemWeeklyNotesBinding
import com.jeong.sesac.sai.model.LibraryNoteUI

class WeeklyNoteAdapter(val callBack : (NoteWithUser) -> Unit) : ListAdapter<NoteWithUser, WeeklyNoteViewHolder>(
    DiffUtilWeeklyNotes()
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyNoteViewHolder {
       return WeeklyNoteViewHolder(ItemWeeklyNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: WeeklyNoteViewHolder, position: Int) {
        val weeklyNoteInfo = currentList[position]
        holder.binding.apply {
            itemWeeklyNoteImg.load(weeklyNoteInfo.image) {
                crossfade(true)
                scale(Scale.FILL)
            }
        root.setOnClickListener {
            callBack(weeklyNoteInfo)
        }
        }
    }
}