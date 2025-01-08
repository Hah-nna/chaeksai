package com.jeong.sesac.sai.ui.searchRegister.register

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.CameraActivity
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentRegisterDetailBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.Dialog
import com.jeong.sesac.sai.util.DialogInterface
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class RegisterDetailFragment :
    BaseFragment<FragmentRegisterDetailBinding>(FragmentRegisterDetailBinding::inflate),
    DialogInterface {
    val args: RegisterDetailFragmentArgs by navArgs()
    var book: String = ""

    /**
     *
     * cameraActivity의 onImageSaved()의 setResult()에서 온 결과가
     * cameraLauncher의 콜백으로 전달됨
     *
     * */
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // result: ActivityResult
        // result.data : CameraActivity가 리턴한 Intent
        if(result.resultCode == RESULT_OK) {
            with(binding) {
                Log.e("TAG-R", result.data?.data.toString())
                // 저장된 이미지의 uri를 이미지뷰에 표시
                ivSelectedImg.setImageURI(result.data?.data)
                ivUploadIcon.visibility = View.INVISIBLE
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.toolbarView.setTitle(R.string.BACK_TOOLBAR_TITLE)
            btnScanBook.clicks().throttleFirst(throttleTime).onEach {
                Log.d("btnScanBook", "click!!!!")
                showDialog()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            toolbar.toolbarView.clicks().onEach {
                findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            cvUploadImg.clicks().throttleFirst(throttleTime).onEach {
                cameraLauncher.launch(Intent(requireContext(), CameraActivity::class.java))
            }.launchIn(lifecycleScope)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }

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

    override fun onClickRightBtn() {
        val rightAction =
            RegisterDetailFragmentDirections.actionFragmentRegisterDetailToFragmentRegisterConfirmation(
                args.libraryName,
                book,
                args.noteContent
            )
        findNavController().navigate(rightAction)
    }
}