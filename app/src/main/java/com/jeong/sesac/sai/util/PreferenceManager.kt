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

        private const val NICKNAME = "nickname"
        private const val USER_ID = "userId"
        private const val LAST_LAT = "last_lat"
        private const val LAST_LNG = "last_lng"


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

    var userId: String
        get() = sp.getString(USER_ID, "").toString()
        set(value) {
            with(spEditor) {
                putString(USER_ID, value)
                Log.d("닉네임 set", nickName)
                 apply()
            }
        }

    var lastLat : Double
        get() = sp.getString(LAST_LAT, "37.566535")?.toDouble() ?: 37.566535
        set(value) {
            with(spEditor) {
                putString(LAST_LAT, value.toString())
                apply()
            }
        }


    var lastLng : Double
        get() = sp.getString(LAST_LNG, "126.977969")?.toDouble() ?: 126.977969
        set(value) {
            with(spEditor) {
                putString(LAST_LNG, value.toString())
                apply()
            }
        }
}