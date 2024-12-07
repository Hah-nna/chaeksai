package com.jeong.sesac.sai.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentSettingsBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.viewmodel.SettingsViewModel

/** writer: 정지영
 *
 * Settings fragment
 * 설정 프레그먼트
 *
 * 마이페이지 -> 설정
 */
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    companion object {
        fun getInstance() = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}