package com.jeong.sesac.sai.util

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.jeong.sesac.sai.CameraActivity
import com.jeong.sesac.sai.CameraMode

class CameraLauncher(
    fragment: Fragment,
    private val onTakePhoto: ((Uri?) -> Unit)? = null,
    private val onBarcodeScan: ((String) -> Unit)? = null
) {
    /**
     *
     * cameraActivity의 onImageSaved()의 setResult()에서 온 결과가
     * cameraLauncher의 콜백으로 전달됨
     *
     * 힌트이미지를 찍고나서 결과 받아오는 함수
     * */
    private val photoCameraLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // result.data.data : CameraActivity가 리턴한 Intent
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { onTakePhoto?.let { it1 -> it1(it) } }
        }
    }

    private val barcodeScanLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        result ->
          result.data?.getStringExtra("barcode_value").let {
              if (it != null) {
                  onBarcodeScan?.let { it1 -> it1(it) }
              }
          }
    }



    // 힌트 이미지 찍을 때와 isbn을 스캔할 때 각각 다르게 카메라를 launch하는 함수
    fun startCameraLauncher(context: Context, mode: CameraMode) {
        val intent = Intent(context, CameraActivity::class.java).apply {
            putExtra("camera_mode", mode.name)
        }
        if (mode == CameraMode.PHOTO_CAPTURE) {
            photoCameraLauncher.launch(intent)
        } else if(mode == CameraMode.BARCODE_SCAN) {
            barcodeScanLauncher.launch(intent)
        }
    }


}
