package com.jeong.sesac.sai.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeong.sesac.sai.data.RecordItem
import com.jeong.sesac.sai.databinding.ItemRecordCardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class RecordAdapter(
    private val items: List<RecordItem>,
    private val onClick: (RecordItem) -> Unit
) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val binding = ItemRecordCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecordViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onViewRecycled(holder: RecordViewHolder) {
        super.onViewRecycled(holder)

        // ViewHolder가 재활용될 때 리소스 정리
        holder.cleanup()
    }

    override fun getItemCount(): Int = items.size

    class RecordViewHolder(
        private val binding: ItemRecordCardBinding,
        private val onClick: (RecordItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root), CoroutineScope {

        // ViewHolder 내에서 사용할 CoroutineScope
        private val job = Dispatchers.Main.immediate
        override val coroutineContext = job

        fun bind(item: RecordItem) {
            with(binding) {

                // 데이터 바인딩
                recordTitle.text = item.title
                recordDescription.text = item.description
                recordImage.setImageResource(item.imageResId)

                // Corbind의 Flow를 활용하여 클릭 이벤트 처리
                val clickEvents = listOf(
                    recordTitle.clicks(),          // 제목 클릭
                    recordDescription.clicks(),    // 설명 클릭
                    recordImage.clicks()           // 이미지 클릭

                    // 여러 Flow를 병합
                ).merge()

                // 병합된 클릭 이벤트를 수집
                launch {
                    clickEvents.collect { onClick(item) }
                }
            }
        }

        /**
         * ViewHolder가 재활용되거나 더 이상 사용되지 않을 때 리소스 정리
         */
        fun cleanup() {

            // CoroutineScope를 취소하여 리소스 정리
            cancel()
        }
    }
}