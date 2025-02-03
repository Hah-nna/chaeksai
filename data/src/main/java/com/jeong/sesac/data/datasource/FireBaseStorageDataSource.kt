package com.jeong.sesac.data.datasource

import android.net.Uri

interface FireBaseStorageDataSource {

    suspend fun createImg(uri : Uri): String
}