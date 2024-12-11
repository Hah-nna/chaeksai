package com.jeong.sesac.sai.ui.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentPrivacyManagementBinding
import com.jeong.sesac.sai.util.BaseFragment

/** writer: 정지영
 *
 * Privacy management fragment
 * 개인정보 관리 프레그먼트
 *
 * 마이페이지 -> 설정 -> 개인정보 관리
 */
class PrivacyManagementFragment :
    BaseFragment<FragmentPrivacyManagementBinding>(FragmentPrivacyManagementBinding::inflate) {

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

        // Toolbar 뒤로가기
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        with(binding) {

            // 닉네임 수정 버튼 클릭 이벤트
            nicknameEditBtn.setOnClickListener {
                toggleNicknameEditMode(true) // 수정 모드 활성화
            }

            saveNicknameBtn.setOnClickListener {
                saveNickname()
            }

            privacyManagementToFollowersBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_privacy_management_to_fragment_followers)
            }

            privacyManagementToLoginManagementBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_privacy_management_to_fragment_login_management)
            }
        }
    }
    private fun toggleNicknameEditMode(enable: Boolean) {
        with(binding) {
            if (enable) {
                nicknameEditText.visibility = View.VISIBLE
                nicknameEditText.setText(nicknameText.text) // 기존 닉네임을 EditText에 설정
                nicknameText.visibility = View.GONE
                saveNicknameBtn.visibility = View.VISIBLE
            } else {
                nicknameEditText.visibility = View.GONE
                nicknameText.visibility = View.VISIBLE
                saveNicknameBtn.visibility = View.GONE
            }
        }
    }

    private fun saveNickname() {
        val newNickname = binding.nicknameEditText.text.toString()
        if (newNickname.isNotBlank()) {
            binding.nicknameText.text = newNickname // 닉네임 업데이트
        }
        toggleNicknameEditMode(false) // 수정 모드 비활성화
    }
}