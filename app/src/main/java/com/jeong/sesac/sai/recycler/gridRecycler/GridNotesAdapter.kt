package com.jeong.sesac.sai.recycler.gridRecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jeong.sesac.sai.databinding.ItemRecentlyFoundNoteBinding
import com.jeong.sesac.sai.util.WeeklyNotesInfo

class GridNotesAdapter(val callBack: (WeeklyNotesInfo) -> Unit) :
    ListAdapter<WeeklyNotesInfo, GridNotesViewHolder>(DiffUtilGridNotes()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GridNotesViewHolder {
        return GridNotesViewHolder(
            ItemRecentlyFoundNoteBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GridNotesViewHolder, position: Int) {
        val recentlyFoundNote = currentList[position]
        holder.binding.apply {
            itemRecentlyFoundNoteImg.setImageResource(recentlyFoundNote.hint_img)
            root.setOnClickListener {
                callBack(recentlyFoundNote)
            }
        }
    }

}