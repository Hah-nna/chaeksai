package com.jeong.sesac.sai.recycler.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeong.sesac.feature.model.PlaceInfo
import com.jeong.sesac.sai.databinding.ItemLibraryInfoBinding
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class MapAdapter(
    val onLibraryNotesCallback: (PlaceInfo) -> Unit,
    val onLibraryBookReviewCallback: (String) -> Unit,
    private val lifecycleScope: LifecycleCoroutineScope
) : ListAdapter<PlaceInfo, MapAdapter.MapViewHolder>(DiffMap) {


    // 여기서 찾기를 눌렀을 때 bottom sheet에 나오는 리사이클러뷰 아이템
    inner class MapViewHolder(val binding : ItemLibraryInfoBinding) : RecyclerView.ViewHolder(binding.root)

    companion object DiffMap : DiffUtil.ItemCallback<PlaceInfo>() {
        override fun areItemsTheSame(oldItem: PlaceInfo, newItem: PlaceInfo) = oldItem == newItem
        override fun areContentsTheSame(oldItem: PlaceInfo, newItem: PlaceInfo) =
            oldItem.id == newItem.id
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        return MapViewHolder(
            ItemLibraryInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        val placeInfoList = currentList[position]

        holder.binding.apply {
            tvLibraryName.text = placeInfoList.place
            tvLibraryAddress.text = placeInfoList.address
            tvPhone.text = placeInfoList.phone

            btnRegister.clicks().throttleFirst(throttleTime).onEach {
                onLibraryNotesCallback(placeInfoList)
            }.launchIn(lifecycleScope)

            btnFind.clicks().throttleFirst(throttleTime).onEach {
                onLibraryBookReviewCallback(placeInfoList.place)
            }.launchIn(lifecycleScope)

        }
    }
}