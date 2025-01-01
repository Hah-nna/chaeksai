package com.jeong.sesac.sai.util

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk

class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        KakaoMapSdk.init(this, "4da58aee4bca744cc8b3764bdb74b99b")
        KakaoSdk.init(this, "4da58aee4bca744cc8b3764bdb74b99b" )

    }
    companion object{
        private lateinit var appInstance : ApplicationClass
        fun getContext() = appInstance

    }
}