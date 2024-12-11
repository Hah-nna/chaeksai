package com.jeong.sesac.sai.recycler.horizontalRecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jeong.sesac.sai.databinding.ItemWeeklyNotesBinding
import com.jeong.sesac.sai.util.WeeklyNotesInfo

class HorizontalNotesAdapter(val callBack : (WeeklyNotesInfo) -> Unit) : ListAdapter<WeeklyNotesInfo, HorizontalNotesViewHolder>(
    DiffUtilHorizontalNotes()
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalNotesViewHolder {
       return HorizontalNotesViewHolder(ItemWeeklyNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HorizontalNotesViewHolder, position: Int) {
        val weeklyNoteInfo = currentList[position]
        holder.binding.apply {
            itemWeeklyNoteImg.setImageResource(weeklyNoteInfo.hint_img)
        root.setOnClickListener {
            callBack(weeklyNoteInfo)
        }
        }
    }
}