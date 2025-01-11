package com.jeong.sesac.sai.ui.searchRegister.register

import android.Manifest.permission.CAMERA
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil3.load
import coil3.request.crossfade
import coil3.request.transformations
import coil3.size.Scale
import coil3.size.SizeResolver
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.jeong.sesac.sai.CameraActivity
import com.jeong.sesac.sai.CameraMode
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentRegisterDetailBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.Dialog
import com.jeong.sesac.sai.util.DialogInterface
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.KakaoMapViewModel
import com.jeong.sesac.sai.viewmodel.RegisterNoteViewModel
import com.jeong.sesac.sai.viewmodel.entity.UiState
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class RegisterDetailFragment :
    BaseFragment<FragmentRegisterDetailBinding>(FragmentRegisterDetailBinding::inflate),
    DialogInterface {

    private val viewModel: RegisterNoteViewModel by activityViewModels {
        appViewModelFactory
    }

    // 전 페이지에서 받아온 arguments
    private val args: RegisterDetailFragmentArgs by navArgs()

    // 책이름
    private var book: String? = null

    // 힌트 이미지
    private var imgUri: Uri? = null
    private var isbn: String? = null

    /**
     *
     * cameraActivity의 onImageSaved()의 setResult()에서 온 결과가
     * cameraLauncher의 콜백으로 전달됨
     *
     * 힌트이미지를 찍고나서 결과 받아오는 함수
     * */
    private val photoCameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        // result.data.data : CameraActivity가 리턴한 Intent
        if (result.resultCode == RESULT_OK) {
            with(binding) {
                // 받아온 이미지uri 저장
                imgUri = result.data?.data
                // 저장된 이미지의 uri를 이미지뷰에 표시
                ivSelectedImg.load(imgUri) {
                    crossfade(true)
                    scale(Scale.FILL)
                    // placeHolder
                    // error(R.Drawable)
                    listener(
                        onSuccess = { _, result ->
                            Log.d("LoadedImageSize",
                                "ImageView size: ${ivSelectedImg.width} x ${ivSelectedImg.height}")
                        }
                    )
                }

//                setImageURI(imgUri)
                Log.d("TAG", "${result.data?.data?.path}")
                ivUploadIcon.visibility = View.INVISIBLE
            }
        }
    }

    /**
     * isbn을 스캔하고 결과를 받아오는 함수
     *
     * */
    private val ISBNScanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == RESULT_OK) {
            result.data?.getStringExtra("barcode_value")?.let { scanResult ->
                isbn = scanResult
            Log.d("ISBN", "$isbn")

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.getBookInfo(scanResult)
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.bookTitleISBNState.collectLatest { state ->
                    when(state) {
                        is UiState.Loading -> {
                            binding.progress.progressCircular.visibility = View.VISIBLE
                        }
                            is UiState.Success -> {
                            binding.progress.progressCircular.visibility = View.GONE
                                val bookInfo = state.data
                                book = bookInfo.firstOrNull()?.title
                                showDialog()
                            }
                        is UiState.Error -> {
                            binding.progress.progressCircular.visibility = View.GONE
                            Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


                // 국립도서관에 isbn으로 책 이름, 이미지 요청 ~
                // -> 성공하면 파베에 올리기
                // -> 실패하면 다시 시도해달라고 토스트 보여주기

        }
    }

    // 힌트 이미지 찍을 때와 isbn을 스캔할 때 각각 다르게 카메라를 launch하는 함수
    private fun startCameraLauncher(mode: CameraMode) {
        val intent = Intent(requireContext(), CameraActivity::class.java).apply {
            putExtra("camera_mode", mode.name)
        }
        when (mode) {
            CameraMode.PHOTO_CAPTURE -> photoCameraLauncher.launch(intent)
            CameraMode.BARCODE_SCAN -> ISBNScanLauncher.launch(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.toolbarView.setTitle(R.string.BACK_TOOLBAR_TITLE)

            // 북스캔버튼을 누를 때
            btnScanBook.clicks().throttleFirst(throttleTime).onEach {
                Log.d("btnScanBook", "click!!!!")
                startCameraLauncher(CameraMode.BARCODE_SCAN)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            toolbar.toolbarView.clicks().onEach {
                findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            // 힌트 사진을 찍을 때
            cvUploadImg.clicks().throttleFirst(throttleTime).onEach {
                startCameraLauncher(CameraMode.PHOTO_CAPTURE)
            }.launchIn(viewLifecycleOwner.lifecycleScope)


        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }

    /**
     * 다이얼로그를 보여주는 함수
     * */
    private fun showDialog() {
        val dialog = Dialog(
            dialogInterface = this,
            title = "$book\n가(이) 맞나요?",
            leftBtnText = "아니오",
            rightBtnText = "네"
        )
        // 다이얼로그의 바깥을 누르거나 뒤로가기를 눌렀을 때 다이얼로그가 닫힐지 말지를 설정
        dialog.isCancelable = false
        // 다이얼로그를 보여줌. 인자로 이 다이얼로그를 관리할 프래그먼트매니저랑 식별할 수 있는 태그를 보내줌
        dialog.show(parentFragmentManager, "Dialog")
    }

    override fun onClickLeftBtn() {
        /** 아니오를 누르면 다이얼로그가 닫힌 다음
         * 다시 바코드 스캔하는 작업을 해야함
         * 일단은 바코드 작업은 아직 적용을 안 했으므로 닫히게만 함
         */
    }

    /**
     *
     * 책 이름이 null, 힌트 이미지, hint.size < 1 인 경우에는 그 다음 페이지로 갈 수 없음
     */

    override fun onClickRightBtn() {
        when (book) {
            null -> {
                Toast.makeText(requireContext(), "책 이름이 없습니다", Toast.LENGTH_SHORT).show()
            }

            else -> {
                val rightAction =
                    RegisterDetailFragmentDirections.actionFragmentRegisterDetailToFragmentRegisterConfirmation(
                        args.libraryName,
                        book!!,
                        args.noteContent
                    )
                findNavController().navigate(rightAction)
            }
        }

    }
}