package com.jeong.sesac.sai.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val throttleTime = 300L

/**
 * Throttle first
 * @param intervalTime -> 입력된 시간동안 이벤트를 진입시키지 않음
 * @return
 */

fun <T>Flow<T>.throttleFirst(intervalTime: Long) : Flow<T> = flow{
    var throttleTime = 0L
    collect { upFlow ->
        val currentTime = System.currentTimeMillis()
        if ((currentTime - throttleTime) > intervalTime) {
            throttleTime = currentTime
            emit(upFlow)
        }
    }
}