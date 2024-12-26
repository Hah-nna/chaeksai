package com.jeong.sesac.sai.recycler.Record

import androidx.recyclerview.widget.DiffUtil
import com.jeong.sesac.sai.util.WeeklyNotesInfo

class DIffRecord : DiffUtil.ItemCallback<WeeklyNotesInfo>() {

    override fun areContentsTheSame(oldItem: WeeklyNotesInfo, newItem: WeeklyNotesInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(oldItem: WeeklyNotesInfo, newItem: WeeklyNotesInfo): Boolean {
        return oldItem == newItem
    }
}