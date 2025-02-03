package com.jeong.sesac.sai.recycler.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import coil3.load
import coil3.request.crossfade
import coil3.size.Scale
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.databinding.ItemRecordCardBinding

class RecordRecyclerAdapter(val callBack: (NoteWithUser) -> Unit) :
    ListAdapter<NoteWithUser, RecordViewHolder>(DIffRecord()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder(
            ItemRecordCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val recordList = currentList[position]
        holder.binding.apply {
           ivRecord.load(recordList.image) {
               crossfade(true)
               scale(Scale.FILL)
           }
            tvTitle.text = recordList.title
            tvContent.text = recordList.content
            root.setOnClickListener {
                callBack(recordList)
            }
        }
    }
}