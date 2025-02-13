package com.jeong.sesac.sai.viewmodel

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.data.repository.KakaoMapRepositoryImpl
import com.jeong.sesac.data.repository.LBSRepositoryImpl
import com.jeong.sesac.feature.model.PlaceInfo
import com.jeong.sesac.sai.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class KakaoMapViewModel(
    private val kakaoMapRepo: KakaoMapRepositoryImpl,
    private val LBSRepo: LBSRepositoryImpl
) : ViewModel() {

    // 현재 위치
    private var _currentLocationState = MutableStateFlow<Location?>(null)
    val currentLocationState = _currentLocationState.asStateFlow()

    // 100m 내의 도서관 정보
    private var _librariesInfoState = MutableStateFlow<UiState<List<PlaceInfo>>>(UiState.Loading)
    val librariesInfoState = _librariesInfoState.asStateFlow()

    /**
     * 현재 유저의 위치에서 100m 내의 도서관 정보 받아오기
     * */
    fun getLibrariesInfo(lng: Double, lat: Double) = viewModelScope.launch {
        _librariesInfoState.value = UiState.Loading
        Log.d("도서관 정보요청", "경도 $lng 위도 $lat")
        kakaoMapRepo.getLibraryInfo(lng, lat)
            .fold(
                onSuccess = { libraries ->
                    _librariesInfoState.value = UiState.Success(libraries)

                },
                onFailure = { e ->
                    _librariesInfoState.value = UiState.Error(e.message ?: "알수 없는 에러 발생")

                }
            )
    }

    fun getCurrentLocation() = viewModelScope.launch {
        LBSRepo.findLocation().collectLatest { location ->
            _currentLocationState.value = location

            getLibrariesInfo(location.longitude, location.latitude)
        }
    }



}
