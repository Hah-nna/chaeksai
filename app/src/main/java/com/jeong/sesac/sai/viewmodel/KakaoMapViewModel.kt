package com.jeong.sesac.sai.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.data.repository.KakaoMapRepositoryImpl
import com.jeong.sesac.feature.model.PlaceInfo
import com.jeong.sesac.sai.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KakaoMapViewModel(private val kakaoMapRepo: KakaoMapRepositoryImpl) : ViewModel() {

    private var _librariesInfoState = MutableStateFlow<UiState<List<PlaceInfo>>>(UiState.Loading)
    val librariesInfoState = _librariesInfoState.asStateFlow()

    private var _selectedPlace = MutableStateFlow<PlaceInfo?>(null)
    val selectedPlace = _selectedPlace.asStateFlow()

    fun getLibrariesInfo(lng: Double, lat: Double) = viewModelScope.launch {
        _librariesInfoState.value = UiState.Loading
        Log.d("도서관 정보요청", "경도 $lng 위도 $lat")
        runCatching {
            kakaoMapRepo.getLibraryInfo(lng, lat)
        }.onSuccess {
            _librariesInfoState.value = UiState.Success(it)
            Log.d("!!!!!", _librariesInfoState.value.toString())
        }.onFailure {
            _librariesInfoState.value = UiState.Error(it.message ?: "알수 없는 에러 발생")
        }.getOrDefault(emptyList())
    }

    fun setSelectedPlace(place: PlaceInfo) {
        _selectedPlace.value = place
    }

}
