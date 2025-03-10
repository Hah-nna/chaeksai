package com.jeong.sesac.sai.ui.libraryMap.bookReview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.CameraMode
import com.jeong.sesac.sai.databinding.FragmentRegisterBookBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.CameraLauncher
import com.jeong.sesac.sai.util.Dialog
import com.jeong.sesac.sai.util.DialogInterface
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.BookViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class RegisterBookFragment :
    BaseFragment<FragmentRegisterBookBinding>(FragmentRegisterBookBinding::inflate),
    DialogInterface {

    private val args: RegisterBookFragmentArgs by navArgs()
    private lateinit var preference: AppPreferenceManager
    private lateinit var cameraLauncher: CameraLauncher
    private val viewModel: BookViewModel by activityViewModels<BookViewModel> {
        appViewModelFactory
    }
    private var isbn = ""

    // 다이얼로그
    private fun showDialog(title: String) {
        val dialog = Dialog.createTwoBtnDialog(
            dialogInterface = this,
            title = title,
            leftBtnText = "취소",
            rightBtnText = "스캔"
        )
        // 다이얼로그의 바깥을 누르거나 뒤로가기를 눌렀을 때 다이얼로그가 닫힐지 말지를 설정
        dialog.isCancelable = false
        // 다이얼로그를 보여줌. 인자로 이 다이얼로그를 관리할 프래그먼트매니저랑 식별할 수 있는 태그를 보내줌
        dialog.show(parentFragmentManager, "Dialog")
    }

    override fun onClickLeftBtn() {
        with(binding) {
            icUploadImg.isVisible = true
            tvBookTitle.isVisible = false
        }
    }

    override fun onClickRightBtn() {
        cameraLauncher.startCameraLauncher(requireActivity(), CameraMode.BARCODE_SCAN)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = AppPreferenceManager.getInstance(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로 가기
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        cameraLauncher = CameraLauncher(
            fragment = this,
            onBarcodeScan = { barcode ->
                isbn = barcode
                viewLifecycleOwner.lifecycleScope.launch { viewModel.getBookTitle(isbn) }
            }
        )
        viewLifecycleOwner.lifecycleScope.launch {
            with(binding) {
                cvImg.clicks().throttleFirst(throttleTime).onEach {
                    cameraLauncher.startCameraLauncher(requireActivity(), CameraMode.BARCODE_SCAN)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                btnComplete.clicks().throttleFirst(throttleTime).onEach {
                        viewModel.createBook(isbn, preference.userId, args.libraryName)
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.uiState.collectLatest {
                            state ->
                            when(state) {
                                is UiState.Loading -> binding.progress.progressCircular.isVisible = true
                                is UiState.Success -> {
                                    binding.progress.progressCircular.isVisible = false
                                    val action = RegisterBookFragmentDirections.fragmentRegisterLibraryBookToFragmentLibraryBookListFragment(args.libraryName)
                                    findNavController().navigate(action)

                                }
                                is UiState.Error -> {
                                    Log.d("등록 에러에러", "에러입니다")
                                }
                            }
                        }
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                viewModel.bookTitle.collect { state ->
                    when (state) {
                        is UiState.Loading -> binding.progress.progressCircular.isVisible = true
                        is UiState.Success -> {
                            with(binding) {
                                binding.progress.progressCircular.isVisible = false
                                icUploadImg.isVisible = false
                                tvBookTitle.isVisible = true
                                tvBookTitle.text = state.data
                            }
                        }
                        is UiState.Error -> {
                            showDialog("책을 찾을 수 없습니다. 다시 스캔하시겠습니까?")
                        }
                        else -> {}
                    }
                }
            }

        }
    }
}