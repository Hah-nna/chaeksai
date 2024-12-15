package com.jeong.sesac.sai.ui.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.sai.data.LoginInfo
import com.jeong.sesac.sai.databinding.FragmentLoginManagementBinding
import com.jeong.sesac.sai.ui.adapter.LoginInfoAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.LOGIN_MANAGEMENT_TOOLBAR_TITLE

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.toolbar.toolbarView) {
            title = LOGIN_MANAGEMENT_TOOLBAR_TITLE
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        // 더미 데이터 생성
        val loginInfoList = listOf(
            LoginInfo("Google", "2024-12-10", true),
            LoginInfo("Naver", "2024-12-08", false),
            LoginInfo("Kakao", "2024-12-01", true)
        )

        // RecyclerView 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = LoginInfoAdapter(loginInfoList)
    }
}