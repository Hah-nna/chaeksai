package com.jeong.sesac.sai.ui.myPage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.data.LoginInfo
import com.jeong.sesac.sai.databinding.FragmentLoginManagementBinding
import com.jeong.sesac.sai.ui.adapter.LoginInfoAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.factory.viewModelFactory
import com.jeong.sesac.sai.viewmodel.MyPageViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.activity.backPresses
import ru.ldralighieri.corbind.appcompat.navigationClicks

/**
 * writer: 정지영
 *
 * Login management fragment
 * 로그인 관리 프래그먼트
 *
 * 마이페이지 -> 설정 -> 개인정보 관리 -> 로그인
 */
class LoginManagementFragment :
    BaseFragment<FragmentLoginManagementBinding>(FragmentLoginManagementBinding::inflate) {

    private val myPageViewModel by activityViewModels<MyPageViewModel>{ viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // 툴바 설정
            toolbar.toolbarView.apply {
                setTitle(R.string.LOGIN_MANAGEMENT_TOOLBAR_TITLE)
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

            // 더미 데이터 생성 및 RecyclerView 설정
            val loginInfoList = listOf(
                LoginInfo("Google", "2024-12-10", true),
                LoginInfo("Naver", "2024-12-08", false),
                LoginInfo("Kakao", "2024-12-01", true)
            )
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = LoginInfoAdapter(loginInfoList)
        }
    }
}