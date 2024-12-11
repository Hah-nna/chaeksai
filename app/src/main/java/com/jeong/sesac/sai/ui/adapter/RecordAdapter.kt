package com.jeong.sesac.sai.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeong.sesac.sai.data.RecordItem
import com.jeong.sesac.sai.databinding.ItemRecordCardBinding

class RecordAdapter(
    private val items: List<RecordItem>,
    private val onClick: (RecordItem) -> Unit
) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val binding =
            ItemRecordCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class RecordViewHolder(
        private val binding: ItemRecordCardBinding,
        private val onClick: (RecordItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecordItem) {
            binding.recordTitle.text = item.title
            binding.recordDescription.text = item.description
            binding.recordImage.setImageResource(item.imageResId)

            // 텍스트를 클릭했을 때
            binding.recordTitle.setOnClickListener { onClick(item) }
            binding.recordDescription.setOnClickListener { onClick(item) }
            // 이미지를 클릭했을 때
            binding.recordImage.setOnClickListener { onClick(item) }
        }
    }
}
