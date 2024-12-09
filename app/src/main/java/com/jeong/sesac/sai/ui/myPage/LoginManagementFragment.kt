package com.jeong.sesac.sai.ui.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeong.sesac.sai.databinding.FragmentLoginManagementBinding
import com.jeong.sesac.sai.util.BaseFragment

/** writer: 정지영
 *
 * Login management fragment
 * 로그인 관리 프레그먼트
 *
 * 마이페이지 -> 설정 -> 개인정보 관리 -> 로그인
 */
class LoginManagementFragment :
    BaseFragment<FragmentLoginManagementBinding>(FragmentLoginManagementBinding::inflate) {

    companion object {
        fun getInstance() = LoginManagementFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentLoginManagementBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}