package com.jeong.sesac.sai.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.sesac.datamodule.repository.KakaoMapRepositoryImpl
import com.jeong.sesac.domain.model.PlaceInfo
import com.jeong.sesac.sai.viewmodel.entity.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KakaoMapViewModel(private val kakaoMapRepo: KakaoMapRepositoryImpl) : ViewModel() {

    private var _librariesInfoState = MutableStateFlow<UiState<List<PlaceInfo>>>(UiState.Loading)
    val librariesInfoState = _librariesInfoState.asStateFlow()


    fun getLibrariesInfo(x : Double, y : Double) = viewModelScope.launch {

        _librariesInfoState.value = UiState.Loading
        runCatching {
            kakaoMapRepo.getLibraryInfo(x,y)
        }.onSuccess {
            _librariesInfoState.value = UiState.Success(it)
            Log.d("!!!!!", _librariesInfoState.value.toString())
        }.onFailure {
            _librariesInfoState.value = UiState.Error(it.message ?: "알수 없는 에러 발생")
        }.getOrDefault(emptyList())
    }
}
