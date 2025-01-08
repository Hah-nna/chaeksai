package com.jeong.sesac.datamodule.api.manager

import com.jeong.sesac.datamodule.api.service.KakaoMapService
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

const val KAKAO_MAP_API_BASE_URL = "https://dapi.kakao.com"

class KakaoMapManager{
    companion object {
        private var retrofitService: KakaoMapService? = null
        fun getInstance() : KakaoMapService {
            if(retrofitService == null) {
                val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(KAKAO_MAP_API_BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()
                retrofitService = retrofit.create(KakaoMapService::class.java)
            }
            return retrofitService!!
        }
    }
}