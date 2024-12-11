package com.jeong.sesac.sai.util

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()

        KakaoMapSdk.init(this, "4da58aee4bca744cc8b3764bdb74b99b")
    }
}