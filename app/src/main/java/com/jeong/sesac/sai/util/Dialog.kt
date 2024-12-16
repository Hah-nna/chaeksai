package com.jeong.sesac.sai.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.jeong.sesac.sai.databinding.DialogBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

// 프래그먼트엫서 상속받아 구현해야할 이벤트들
interface DialogInterface {
    fun onClickLeftBtn()
    fun onClickRightBtn()
}

/**
 *  아래의 블로그를 참고해서 커스텀 다이얼로그를 만듦
 *  https://velog.io/@nahy-512/AndroidKotlin-%EC%BB%A4%EC%8A%A4%ED%85%80-%EB%8B%A4%EC%9D%B4%EC%96%BC%EB%A1%9C%EA%B7%B8-%EB%A7%8C%EB%93%A4%EA%B8%B0
 *
 * */
class Dialog(
    private val dialogInterface : DialogInterface,
    private val title: String,
    private val leftBtnText: String,
    private val rightBtnText: String,
) : DialogFragment() {

    private var _binding: DialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogBinding.inflate(inflater, container, false)

        /**
         * dialog의 백드라운드 지정
         * */
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setupDialog()
        setupClickListeners()
        return binding.root
    }

    /**
     * 다이얼로그를 다른 프래그먼트에서 사용할 때,
     * 다이얼로그의 타이틀, 왼쪽 버튼 텍스트, 오른쪽 버튼 텍스트를 지정함
     * */
    private fun setupDialog() {
            with(binding) {
                tvDialog.text = title
                btnLeft.text = leftBtnText
                btnRight.text = rightBtnText
        }

    }

    /**
     * 각 버튼마다 클릭이벤트를 달아줌
     * 근데 아까 위에서 선언한 인터페이스를 구현(onClickLeftBtn, onClickRightBtn)
     * 각 클릭이벤트 후 dissmiss()를 해주어 다이얼르그를 뷰에서 삭제함
     * */
    private fun setupClickListeners() {
        binding.btnLeft.clicks().onEach {
            /**
             * 사용하려는 프랙르먼트에서 dialogInterface를 상속받아 onClickLeftBtn()안에 원하는 것을 구현하면 됨
             * */
            dialogInterface.onClickLeftBtn()
            dismiss()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.btnRight.clicks().onEach {
            /**
             * 사용하려는 프랙르먼트에서 dialogInterface를 상속받아 onClickRightBtn()안에 원하는 것을 구현하면 됨
             * */
            dialogInterface.onClickRightBtn()
            dismiss()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    /**
     * 해당 프래그먼트의 뷰가 사라지지만 binding은 여전히 뷰(프래그먼트)를 참조함
     * 따라서 _binding = null로 만들어서 뷰에 대한 참조를 없앰
     * */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

