package com.jeong.sesac.sai.ui.searchRegister.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentRegisterDetailBinding
import com.jeong.sesac.sai.util.BACK_TOOLBAR_TITLE
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.Dialog
import com.jeong.sesac.sai.util.DialogInterface
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class RegisterDetailFragment :
    BaseFragment<FragmentRegisterDetailBinding>(FragmentRegisterDetailBinding::inflate),
    DialogInterface {
    val args: RegisterDetailFragmentArgs by navArgs()
    var book: String = "물고기는 존재하지 않는다"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.toolbarView.title = BACK_TOOLBAR_TITLE
            btnScanBook.clicks().onEach {
                showDialog()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            toolbar.toolbarView.clicks().onEach {
                findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

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