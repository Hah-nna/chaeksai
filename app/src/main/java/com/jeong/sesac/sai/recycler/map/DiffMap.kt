package com.jeong.sesac.sai.recycler.map

import androidx.recyclerview.widget.DiffUtil
import com.jeong.sesac.domain.model.PlaceInfo

class DiffMap : DiffUtil.ItemCallback<PlaceInfo>() {
    override fun areItemsTheSame(oldItem: PlaceInfo, newItem: PlaceInfo) = oldItem == newItem
    override fun areContentsTheSame(oldItem: PlaceInfo, newItem: PlaceInfo) =
        oldItem.id == newItem.id
}