package com.jeong.sesac.sai.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager

class AppPreferenceManager {
    companion object {
        private lateinit var manager : AppPreferenceManager
        private lateinit var sp : SharedPreferences
        private lateinit var spEditor : SharedPreferences.Editor

        fun getInstance(context : Context) : AppPreferenceManager {
            if(this::manager.isInitialized) {
                return manager
            } else {
                sp = PreferenceManager.getDefaultSharedPreferences(context)
                spEditor  = sp.edit()
                manager = AppPreferenceManager()
            }
            return manager
        }
        private const val NICKNAME = "nickname"
    }

    var nickName : String
        get() = sp.getString(NICKNAME, "").toString()

        set(value) {
           with(spEditor) {
               putString(NICKNAME, value)
               Log.d("닉네임 set", nickName)
               apply()
           }
        }
}