package com.jeong.sesac.sai.ui.libraryMap.writeNote

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil3.load
import coil3.request.crossfade
import coil3.size.Scale
import com.jeong.sesac.sai.CameraMode
import com.jeong.sesac.sai.databinding.FragmentLibraryMapWriteNoteBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.CameraLauncher
import com.jeong.sesac.sai.util.Dialog
import com.jeong.sesac.sai.util.DialogInterface
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.NoteViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class LibraryWriteNoteFragment :
    BaseFragment<FragmentLibraryMapWriteNoteBinding>(FragmentLibraryMapWriteNoteBinding::inflate),
    DialogInterface {

    private lateinit var preference: AppPreferenceManager

    // 전 페이지에서 받아온 arguments
    private val args: LibraryWriteNoteFragmentArgs by navArgs()

    // 이미지
    private var imgUri: Uri? = null

    // 카메라 헬퍼 클래스
    private lateinit var cameraLauncher: CameraLauncher

    private val viewModel: NoteViewModel by viewModels<NoteViewModel> {
        appViewModelFactory
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = AppPreferenceManager.getInstance(requireContext())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("도서관이름:", " ${args.libraryName}")

        cameraLauncher = CameraLauncher(this) { uri ->
            imgUri = uri
            with(binding) {
                ivImg.load(uri) {
                    crossfade(true)
                    scale(Scale.FILL)
                }
                icUploadImg.visibility = View.INVISIBLE
            }
        }

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
                cameraLauncher.startCameraLauncher(requireActivity(), CameraMode.PHOTO_CAPTURE)
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