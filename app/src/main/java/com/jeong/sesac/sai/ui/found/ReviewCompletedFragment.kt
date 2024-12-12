package com.jeong.sesac.sai.ui.found

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.databinding.FragmentReviewCompletedBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.Dialog
import com.jeong.sesac.sai.util.DialogInterface

/**
 *  DialogInterface를 상속받아 다이얼로그 속 onClickLeftBtn, onClickRighttBtn을 구현함
 * */
class ReviewCompletedFragment : BaseFragment<FragmentReviewCompletedBinding>(FragmentReviewCompletedBinding::inflate),
    DialogInterface {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewCompletedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnCompletedReview.setOnClickListener {
                showDialog()
            }

        }
    }

    /**
     * 해당 프래그먼트에서 보여줄 다이얼로그 속 타이틀과 버튼 텍스트를 정의해줌
     *
     * */
    private fun showDialog() {
        val dialog = Dialog(

            /**
             * dialogInterface를 상속받은 객체가 ReviewCompletedFragment이기 때문에 this라고 적음
             * 즉, 리뷰남기기 버튼이 눌렸을 때 호출할 메서드들이 정의된 객체를 전달하는 것임
             * */
            dialogInterface = this,
            title = "쪽지를 남기고 등록하면\n추가 힌트 교환권 증정 !",
            leftBtnText = "나중에",
            rightBtnText = "쪽지쓰러가기"
        )
        // 다이얼로그의 바깥을 누르거나 뒤로가기를 눌렀을 때 다이얼로그가 닫힐지 말지를 설정
        dialog.isCancelable = true
        // 다이얼로그를 보여줌. 인자로 이 다이얼로그를 관리할 프래그먼트매니저랑 식별할 수 있는 태그를 보내줌
        dialog.show(parentFragmentManager, "Dialog")
    }

    /**
     * dialogInterface의 이벤트들을 정의해주어서
     * 왼쪽과 오른쪽을 클릭할 때 각각 다르게 작동하도록 함
     * */
    override fun onClickLeftBtn() {
        val leftAction = ReviewCompletedFragmentDirections.actionFragmentReviewCompletedToFragmentHome()
        findNavController().navigate(leftAction)
    }

    override fun onClickRightBtn() {
        val rightAction =  ReviewCompletedFragmentDirections.actionFragmentReviewCompletedToFragmentSearchRegister("")
        findNavController().navigate(rightAction)
    }
}