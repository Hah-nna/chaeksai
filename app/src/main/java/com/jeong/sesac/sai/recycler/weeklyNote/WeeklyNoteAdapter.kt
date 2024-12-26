package com.jeong.sesac.sai.recycler.weeklyNote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jeong.sesac.sai.databinding.ItemWeeklyNotesBinding
import com.jeong.sesac.sai.util.WeeklyNotesInfo

class WeeklyNoteAdapter(val callBack : (WeeklyNotesInfo) -> Unit) : ListAdapter<WeeklyNotesInfo, WeeklyNoteViewHolder>(
    DiffUtilWeeklyNotes()
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyNoteViewHolder {
       return WeeklyNoteViewHolder(ItemWeeklyNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: WeeklyNoteViewHolder, position: Int) {
        val weeklyNoteInfo = currentList[position]
        holder.binding.apply {
            itemWeeklyNoteImg.setImageResource(weeklyNoteInfo.hint_img)
        root.setOnClickListener {
            callBack(weeklyNoteInfo)
        }
        }
    }
}