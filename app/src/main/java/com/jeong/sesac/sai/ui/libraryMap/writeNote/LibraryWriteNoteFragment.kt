package com.jeong.sesac.sai.ui.libraryMap.writeNote

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil3.load
import coil3.request.crossfade
import coil3.size.Scale
import com.jeong.sesac.sai.CameraActivity
import com.jeong.sesac.sai.CameraMode
import com.jeong.sesac.sai.databinding.FragmentLibraryMapWriteNoteBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.ChakSaiClass
import com.jeong.sesac.sai.util.Dialog
import com.jeong.sesac.sai.util.DialogInterface
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.WriteNoteViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class LibraryWriteNoteFragment :
    BaseFragment<FragmentLibraryMapWriteNoteBinding>(FragmentLibraryMapWriteNoteBinding::inflate),
    DialogInterface {

    private val viewModel: WriteNoteViewModel by viewModels<WriteNoteViewModel> {
        appViewModelFactory
    }

    // 전 페이지에서 받아온 arguments
    private val args: LibraryWriteNoteFragmentArgs by navArgs()

    // 이미지
    private var imgUri: Uri? = null

    private lateinit var preference: AppPreferenceManager

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
                ivImg.load(imgUri) {
                    crossfade(true)
                    scale(Scale.FILL)
                    listener(
                        onSuccess = { _, result ->
                            Log.d(
                                "LoadedImageSize",
                                "ImageView size: ${ivImg.width} x ${ivImg.height}"
                            )
                        }
                    )
                }

//                setImageURI(imgUri)
                Log.d("TAG", "${result.data?.data?.path}")
                icUploadImg.visibility = View.INVISIBLE
            }
        }
    }


    // 힌트 이미지 찍을 때와 isbn을 스캔할 때 각각 다르게 카메라를 launch하는 함수
    private fun startCameraLauncher(mode: CameraMode) {
        val intent = Intent(requireContext(), CameraActivity::class.java).apply {
            putExtra("camera_mode", mode.name)
        }
        if (mode == CameraMode.PHOTO_CAPTURE) {
            photoCameraLauncher.launch(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = AppPreferenceManager.getInstance(requireContext())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("도서관이름:", " ${args.libraryName}")


        with(binding) {
            btnComplete.clicks().throttleFirst(throttleTime).onEach {
                val isValid = when {
                    etvTitle.text.isNullOrEmpty() -> {
                        showDialog("제목을 입력해주세요")
                        false
                    }
                    tvNoteContent.text.isNullOrEmpty() -> {
                        showDialog("내용을 입력해주세요")
                        false
                    }
                    else -> true
                }

                if (isValid) {
                    viewModel.createNote(
                        imgUri?.toString() ?: "",
                        etvTitle.text.toString(), tvNoteContent.text.toString(),
                        args.libraryName, preference.nickName
                    )

                    viewModel.uiState.collectLatest { state ->
                        when (state) {
                            is UiState.Loading -> binding.progress.progressCircular.isVisible = true
                            is UiState.Success -> {
                                binding.progress.progressCircular.isVisible = false
                                val action =
                                    LibraryWriteNoteFragmentDirections.actionFragmentLibraryWriteNoteToHome()
                                findNavController().navigate(action)
                            }

                            is UiState.Error -> {
                                binding.progress.progressCircular.isVisible = false
                                Toast.makeText(requireContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }


            }.launchIn(viewLifecycleOwner.lifecycleScope)

            toolbar.toolbarView.clicks().onEach {
                findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            //  사진을 찍을 때
            ivImg.clicks().throttleFirst(throttleTime).onEach {


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
    private fun showDialog(title: String) {
        val dialog = Dialog.createSingleBtnDialog(
            dialogInterface = this,
            title = title,
            btnText = "확인"
        )
        // 다이얼로그의 바깥을 누르거나 뒤로가기를 눌렀을 때 다이얼로그가 닫힐지 말지를 설정
        dialog.isCancelable = false
        // 다이얼로그를 보여줌. 인자로 이 다이얼로그를 관리할 프래그먼트매니저랑 식별할 수 있는 태그를 보내줌
        dialog.show(parentFragmentManager, "Dialog")
    }

    override fun onClickLeftBtn() {
    }

    override fun onClickRightBtn() {
    }


}