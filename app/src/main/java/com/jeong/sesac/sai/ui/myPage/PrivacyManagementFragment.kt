package com.jeong.sesac.sai.ui.myPage

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentPrivacyManagementBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.PRIVACY_MANAGEMENT_TOOLBAR_TITLE
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.activity.backPresses
import ru.ldralighieri.corbind.appcompat.navigationClicks
import ru.ldralighieri.corbind.view.clicks

/**
 * writer: 정지영
 *
 * Privacy management fragment
 * 개인정보 관리 프레그먼트
 *
 * 마이페이지 -> 설정 -> 개인정보 관리
 */
class PrivacyManagementFragment :
    BaseFragment<FragmentPrivacyManagementBinding>(FragmentPrivacyManagementBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            // 툴바 설정
            toolbar.toolbarView.apply {
                title = PRIVACY_MANAGEMENT_TOOLBAR_TITLE
                navigationClicks()
                    .onEach { findNavController().navigateUp() }
                    .launchIn(lifecycleScope)
            }

            // 뒤로가기 버튼 동작
            requireActivity().onBackPressedDispatcher.backPresses(viewLifecycleOwner)
                .onEach { findNavController().navigateUp() }
                .launchIn(lifecycleScope)

            // 닉네임 수정 버튼 클릭 이벤트
            nicknameEditBtn.clicks()
                .onEach { toggleNicknameEditMode(true) }
                .launchIn(lifecycleScope)

            // 닉네임 저장 버튼 클릭 이벤트
            saveNicknameBtn.clicks()
                .onEach { saveNickname() }
                .launchIn(lifecycleScope)

            // 팔로워 화면 이동 버튼 클릭 이벤트
            privacyManagementToFollowersBtn.clicks()
                .onEach {
                    findNavController().navigate(
                        R.id.action_fragment_privacy_management_to_fragment_followers
                    )
                }
                .launchIn(lifecycleScope)

            // 로그인 관리 화면 이동 버튼 클릭 이벤트
            privacyManagementToLoginManagementBtn.clicks()
                .onEach {
                    findNavController().navigate(
                        R.id.action_fragment_privacy_management_to_fragment_login_management
                    )
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun toggleNicknameEditMode(enable: Boolean) {
        with(binding) {
            nicknameEditText.visibility = if (enable) View.VISIBLE else View.GONE
            nicknameText.visibility = if (enable) View.GONE else View.VISIBLE
            saveNicknameBtn.visibility = if (enable) View.VISIBLE else View.GONE

            if (enable) {

                // 기존 닉네임을 EditText에 설정
                nicknameEditText.setText(nicknameText.text)
            }
        }
    }

    private fun saveNickname() {
        val newNickname = binding.nicknameEditText.text.toString()
        if (newNickname.isNotBlank()) {

            // 닉네임 업데이트
            binding.nicknameText.text = newNickname
        }

        // 수정 모드 비활성화
        toggleNicknameEditMode(false)
    }
}