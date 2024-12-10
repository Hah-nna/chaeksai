package com.jeong.sesac.sai.recycler.weeklynotes

import androidx.recyclerview.widget.DiffUtil
import com.jeong.sesac.sai.util.WeeklyNotesInfo

class DiffUtilWeeklyNotes : DiffUtil.ItemCallback<WeeklyNotesInfo>(){

    override fun areItemsTheSame(oldItem: WeeklyNotesInfo, newItem: WeeklyNotesInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WeeklyNotesInfo, newItem: WeeklyNotesInfo): Boolean {
        return oldItem == newItem
    }
}