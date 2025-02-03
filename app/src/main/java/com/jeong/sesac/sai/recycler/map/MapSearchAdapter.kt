package com.jeong.sesac.sai.recycler.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeong.sesac.feature.model.PlaceInfo
import com.jeong.sesac.sai.databinding.ItemSearchPlaceBinding
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class MapSearchAdapter(
    private val lifecycleScope : LifecycleCoroutineScope,
    val callback : (PlaceInfo) -> Unit
) : ListAdapter<PlaceInfo, MapSearchAdapter.MapSearchListItemViewHolder>(DiffMapSeacrch) {

   inner class MapSearchListItemViewHolder(val binding : ItemSearchPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    companion object DiffMapSeacrch : DiffUtil.ItemCallback<PlaceInfo>() {
        override fun areItemsTheSame(oldItem: PlaceInfo, newItem: PlaceInfo): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: PlaceInfo, newItem: PlaceInfo): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapSearchListItemViewHolder {
        return MapSearchListItemViewHolder(ItemSearchPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MapSearchListItemViewHolder, position: Int) {
        val searchPlaceInfoList = currentList[position]
        holder.binding.apply {
            tvPlaceName.text = searchPlaceInfoList.place
            tvPlaceAddress.text = searchPlaceInfoList.address
            root.clicks().throttleFirst(throttleTime).onEach {
                callback(searchPlaceInfoList)
            }.launchIn(lifecycleScope)
        }
    }
}