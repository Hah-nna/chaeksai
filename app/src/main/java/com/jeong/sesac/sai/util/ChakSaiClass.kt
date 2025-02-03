package com.jeong.sesac.sai.util

import android.app.Application
import com.jeong.sesac.sai.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk

class ChakSaiClass : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY )

    }
    companion object{
        private lateinit var appInstance : ChakSaiClass
        fun getContext() = appInstance

    }
}