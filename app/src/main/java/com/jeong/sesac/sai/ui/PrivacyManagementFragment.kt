package com.jeong.sesac.sai.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentPrivacyManagementBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.viewmodel.PrivacyManagementViewModel

/** writer: 정지영
 *
 * Privacy management fragment
 * 개인정보 관리 프레그먼트
 *
 * 마이페이지 -> 설정 -> 개인정보 관리
 */
class PrivacyManagementFragment :
    BaseFragment<FragmentPrivacyManagementBinding>(FragmentPrivacyManagementBinding::inflate) {

    companion object {
        fun getInstance() = PrivacyManagementFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentPrivacyManagementBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            privacyManagementToFollowersBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_privacy_management_to_fragment_followers)
            }

            privacyManagementToLoginManagementBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_privacy_management_to_fragment_login_management)
            }
        }
    }
}