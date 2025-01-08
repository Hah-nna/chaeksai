package com.jeong.sesac.sai.recycler.recentlyFoundNote

import androidx.recyclerview.widget.DiffUtil
import com.jeong.sesac.sai.util.WeeklyNotesInfo

class DiffUtilRecentlyFoundNote : DiffUtil.ItemCallback<WeeklyNotesInfo>() {
    override fun areItemsTheSame(oldItem: WeeklyNotesInfo, newItem: WeeklyNotesInfo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WeeklyNotesInfo, newItem: WeeklyNotesInfo): Boolean {
        return oldItem.id == newItem.id
    }

}