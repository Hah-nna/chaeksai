package com.jeong.sesac.sai.recycler.recentlyFoundNotesRecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jeong.sesac.sai.databinding.ItemRecentlyFoundNoteBinding
import com.jeong.sesac.sai.util.WeeklyNotesInfo

class RecentlyFoundNotesAdapter(val callBack: (WeeklyNotesInfo) -> Unit) :
    ListAdapter<WeeklyNotesInfo, RecentlyFoundNotesViewHolder>(DiffUtilRecentlyFoundNotes()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentlyFoundNotesViewHolder {
        return RecentlyFoundNotesViewHolder(
            ItemRecentlyFoundNoteBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecentlyFoundNotesViewHolder, position: Int) {
        val recentlyFoundNote = currentList[position]
        holder.binding.apply {
            itemRecentlyFoundNoteImg.setImageResource(recentlyFoundNote.hint_img)
            root.setOnClickListener {
                callBack(recentlyFoundNote)
            }
        }
    }

}