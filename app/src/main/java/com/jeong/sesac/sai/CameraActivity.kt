package com.jeong.sesac.sai

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.lifecycleScope
import com.jeong.sesac.sai.databinding.ActivityCameraBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import java.util.Locale

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    // 사진 촬영에 관한 useCase
    // 기본적인 사진촬영에 관한 객체
    private var imgCapture: ImageCapture? = null

    // 카메라 작업을 위한 백그라운드 스레드 실행하는 역할
    /**
     * Executor(java) :
     * 주어진 명령을 미래의 어느 시점에 실행
     * 명령은 Executor 재량에 따라 새로운 스레드, 풀링된 스레드, 혹은 호출 스레드에서 실행될 수 있음
     *
     * */
    private lateinit var cameraExecutor: ExecutorService

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSION =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                // 안드로이드 버전 9이상인 경우
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        // 모든 권한 체크
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            // ActivityCompat : Activity의 features에 엑세스 하도록 도와주는 헬퍼
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSIONS
            )
        }
        binding.imageCaptureButton.clicks().onEach { takePhoto() }.launchIn(lifecycleScope)
    }

    override fun onStart() {
        super.onStart()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        // 이미지 캡쳐가 설정되기 전에 사진 버튼을 탭하면 null -> 카메라가 준비되지 않으면 종료
        val imgCapture = imgCapture ?: return

        // 이미지를 보관할 MediaStore
        // MediaStore에 표시되는 이름이 고유해야하니까 타임스탬프 사용
        // MediaStore : 미디어 파일을 관리하는 데이터베이스
        // ContentValues() : 데이터베이스에 값을 저장할 때 쓰는 컨테이너
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.KOREAN)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")

            // android 9 이상에서 실행할 코드
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // 원하는 출력방법을 지정할 수 있음(사진을 어떻게 저장할건지 설정)
        // 외부저장소에 있는 이미지들의 위치에 contentValues(저장할 이미지의 정보(이름, 형식)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        /**
         *
         * object : ImageCapture.OnImageSavedCallback
         * -> 사진이 저장된 후 어떻게 할지 정의하는 콜백
         * */
        imgCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                // 사진 찍기 실패했을 때
                override fun onError(exc: ImageCaptureException) {
                    Log.d("TAG-C-Error", "${exc.message}")
                }

                // 사진 저장이 성공했을 때
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                    // 저장된 이미지의 uri(위치)를 가져옴
                    val savedUri = outputFileResults.savedUri

                    // 여기서 uri 가져와서 coil로 자르고 저장해서 setData 하기

                    // 결과를 이전 화면으로 전달하기 위한 Intent를 만듦
                    // Intent : 한 화면(액티비티)에서 다른 화면으로 정보를 전달하는 역할그
                    val resultIntent = Intent().apply {
                        //putExtra("imgUrl", savedUri)
                        // 저장된 이미지의 uri를 Intent에 첨부
                        setData(savedUri)
                    }
                    val msg = "이미지 캡쳐 성공성공 : ${outputFileResults.savedUri}"
                    Log.e("TAG-C-S", savedUri.toString())
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

                    // 결과를 설정하고 현재 액티비티(camera Activity)를 종료함
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }
        )
    }

    private fun startCamera() {
        // 카메라를 설치하려면 시간 걸림 -> 나중에 완료될 작업이라는 것을 알려줌
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this@CameraActivity)

        // 카메라가 준비되면 실행할 작업(addListener({...}))
        cameraProviderFuture.addListener({
            // 라이프사이클오너와 카메라 라이프사이클을 바인딩
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // 프리뷰
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imgCapture = ImageCapture.Builder().build()

            // 후면 카메라를 기본으로 셋팅
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            /**
             * 기존에 실행중이던 카메라 연경 해제
             * 다시 새로운 설정으로 연결
             * 왜냐면 이전 설정이 남아있으면 충돌 발생가능
             * */
            try {
                // 기존의 바인딩을 모두 해제
                cameraProvider.unbindAll()

                // 새로운 바인딩을 생성
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imgCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "권한 승인 안 함", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }

    override fun onStop() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}