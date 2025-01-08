package com.jeong.sesac.sai.recycler.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jeong.sesac.domain.model.PlaceInfo
import com.jeong.sesac.sai.databinding.ItemLibraryInfoBinding
import com.jeong.sesac.sai.dto.LibraryInfo

class MapAdapter(
    val onRegisterCallback: (PlaceInfo) -> Unit,
//    val onFindCallback: (LibraryInfo) -> Unit
) : ListAdapter<PlaceInfo, MapViewHolder>(DiffMap()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        return MapViewHolder(
            ItemLibraryInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        val placeInfoList = currentList[position]

        holder.binding.apply {
            tvLibraryName.setText(placeInfoList.place_name)
            tvLibraryAddress.setText(placeInfoList.address_name)
            tvPhone.setText(placeInfoList.phone)

            btnRegister.setOnClickListener {
                onRegisterCallback(placeInfoList)
            }
//            btnFind.setOnClickListener {
//                onFindCallback(libraryInfoList)
//            }
        }
    }
}