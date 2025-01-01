package com.jeong.sesac.sai.recycler.recentlyFoundNote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jeong.sesac.sai.databinding.ItemRecentlyFoundNoteBinding
import com.jeong.sesac.sai.util.WeeklyNotesInfo

class RecentlyFoundNoteAdapter(val callBack: (WeeklyNotesInfo) -> Unit) :
    ListAdapter<WeeklyNotesInfo, RecentlyFoundNoteViewHolder>(DiffUtilRecentlyFoundNote()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentlyFoundNoteViewHolder {
        return RecentlyFoundNoteViewHolder(
            ItemRecentlyFoundNoteBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecentlyFoundNoteViewHolder, position: Int) {
        val recentlyFoundNote = currentList[position]
        holder.binding.apply {
            itemRecentlyFoundNoteImg.setImageResource(recentlyFoundNote.hint_img)
            root.setOnClickListener {
                callBack(recentlyFoundNote)
            }
        }
    }

}