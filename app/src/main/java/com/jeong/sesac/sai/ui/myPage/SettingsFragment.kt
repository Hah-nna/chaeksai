package com.jeong.sesac.sai.ui.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentSettingsBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.MyPageViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.activity.backPresses
import ru.ldralighieri.corbind.appcompat.navigationClicks
import ru.ldralighieri.corbind.view.clicks

/**
 * writer: 정지영
 *
 * SettingsFragment 클래스
 * 마이페이지 -> 설정 화면을 담당하는 프래그먼트
 */
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            // 툴바 설정
            toolbar.toolbarView.apply {
                setTitle(R.string.SETTING_MANAGEMENT_TOOLBAR_TITLE)

                // Corbind 활용
                navigationClicks()
                    .throttleFirst(throttleTime)
                    .onEach { findNavController().navigateUp() }
                    .launchIn(lifecycleScope)
            }

            // 뒤로가기 버튼 동작
            requireActivity().onBackPressedDispatcher.backPresses(viewLifecycleOwner)
                .throttleFirst(throttleTime)
                .onEach { findNavController().navigateUp() }
                .launchIn(lifecycleScope)

            // 개인정보 관리 화면 이동 버튼
            settingsToPrivacyManagementBtn.clicks()
                .throttleFirst(throttleTime)
                .onEach {
                    findNavController()
                        .navigate(R.id.action_fragment_settings_to_fragment_privacy_management)
                }
                .launchIn(lifecycleScope)

            // 앱 설정 화면 이동 버튼
            settingsToAppSettingsBtn.clicks()
                .throttleFirst(throttleTime)
                .onEach {
                    findNavController()
                        .navigate(R.id.action_fragment_settings_to_fragment_app_settings)
                }
                .launchIn(lifecycleScope)
        }
    }
}