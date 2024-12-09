package com.jeong.sesac.sai.ui.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeong.sesac.sai.databinding.FragmentAppSettingsBinding
import com.jeong.sesac.sai.util.BaseFragment

/** writer: 정지영
 *
 * App settings fragment
 * 앱 설정 프래그먼트
 *
 * 마이페이지 -> 설정 -> 앱 설정
 */
class AppSettingsFragment :
    BaseFragment<FragmentAppSettingsBinding>(FragmentAppSettingsBinding::inflate) {

    companion object {
        fun getInstance() = AppSettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentAppSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}