package com.jeong.sesac.sai.recycler.Record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jeong.sesac.sai.databinding.ItemRecordCardBinding
import com.jeong.sesac.sai.util.WeeklyNotesInfo

class RecordRecyclerAdapter(val callBack: (WeeklyNotesInfo) -> Unit) :
    ListAdapter<WeeklyNotesInfo, RecordViewHolder>(DIffRecord()) {


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
           ivRecord.setImageResource(recordList.hint_img)
            root.setOnClickListener {
                callBack(recordList)
            }
        }
    }
}