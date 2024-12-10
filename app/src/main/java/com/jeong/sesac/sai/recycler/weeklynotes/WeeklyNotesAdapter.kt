package com.jeong.sesac.sai.recycler.weeklynotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jeong.sesac.sai.databinding.ItemWeeklyNotesBinding
import com.jeong.sesac.sai.util.WeeklyNotesInfo

class WeeklyNotesAdapter(val callBack : (WeeklyNotesInfo) -> Unit) : ListAdapter<WeeklyNotesInfo, WeeklyNotesViewHolder>(
    DiffUtilWeeklyNotes()
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyNotesViewHolder {
       return WeeklyNotesViewHolder(ItemWeeklyNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: WeeklyNotesViewHolder, position: Int) {
        val weeklyNoteInfo = currentList[position]
        holder.binding.apply {
            itemWeeklyNoteImg.setImageResource(weeklyNoteInfo.hint_img)
        root.setOnClickListener {
            callBack(weeklyNoteInfo)
        }
        }
    }
}