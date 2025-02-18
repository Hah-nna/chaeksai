package com.jeong.sesac.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


class LBSRepositoryImpl(private val context: Context) {

    /**
     * 현재 유저의 위치 업데이트
     * */
    fun findLocation() = callbackFlow {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 10000L
        ).run {
            setMinUpdateDistanceMeters(10F)
            setWaitForAccurateLocation(true)
//            setMinUpdateIntervalMillis(10000L)
//            setMaxUpdateDelayMillis(12000L)
                .build()
        }

    val currentFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    Log.d("현재 경도위도", "현재경도위도: lat=${location.latitude}, lng=${location.longitude}")
                    trySend(location)
                }
            }
        }

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            currentFusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
        awaitClose {
            currentFusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}