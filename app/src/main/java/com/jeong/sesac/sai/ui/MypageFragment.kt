package com.jeong.sesac.sai.ui

import ReviewAdapter
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.data.Review
import com.jeong.sesac.sai.databinding.FragmentMypageBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class MypageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // viewModel에서 isLoggedIn를 받을 떄마다 ㄱ
//        viewModel.loginState.onEach { loginState ->
//            setupLoginState(loginState)
//        }.launchIn(lifecycleScope)

        setBlurView()
        setRecyclerView()

        with(binding) {
            // Corbind를 사용하여 설정 버튼 클릭 이벤트 처리
            btnSetting.clicks()
                .throttleFirst(throttleTime)
                .onEach {
                    val action = MypageFragmentDirections.actionFragmentMyPageToFragmentSettings()
                    findNavController().navigate(action)
                }.launchIn(lifecycleScope)

            // Corbind를 사용하여 리뷰 더보기 버튼 클릭 이벤트 처리
            btnReviewMore.clicks()
                .throttleFirst(throttleTime)
                .onEach {
                    val action =
                        MypageFragmentDirections.actionFragmentMyPageToFragmentReceivedReviews()
                    findNavController().navigate(action)
                }.launchIn(lifecycleScope)

        }


//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.loginState.collectLatest {
//                when (it) {
//                    is UiState.Loading -> {
//                        binding.progressBar.progressCircular.visibility = View.VISIBLE
//                    }
//
//                    is UiState.Error -> {
//                        with(binding) {
//                            loggedInLayout.visibility = View.INVISIBLE
//                            nonLoggedContainer.visibility = View.VISIBLE
//                            tvReceivedReview.visibility = View.INVISIBLE
//                            btnReviewMore.visibility = View.INVISIBLE
//                            blurView.visibility = View.VISIBLE
//                            tvNonLoginRecyclerView.visibility = View.VISIBLE
//                        }
//                    }
//
//                    is LoginState.Success -> {
//                        with(binding) {
//                            progressBar.progressCircular.visibility = View.GONE
//                            loggedInLayout.visibility = View.VISIBLE
//                            nonLoggedContainer.visibility = View.GONE
//                            tvReceivedReview.visibility = View.VISIBLE
//                            btnReviewMore.visibility = View.VISIBLE
//                            blurView.visibility = View.GONE
//                            tvNonLoginRecyclerView.visibility = View.GONE
//                        }
//                    }
//
//                    is LoginState.Error -> {
//                        throw Error("에러발생")
//                    }
//
//                }
//            }
//        }
//
    }

    private fun setRecyclerView() {
        // 더미 데이터 생성
        val reviews = listOf(
            Review(R.drawable.ic_profile, "User1", "Great app!"),
            Review(R.drawable.ic_profile, "User2", "Loved it!"),
            Review(R.drawable.ic_profile, "User3", "Needs improvement.")
        )

        // RecyclerView 설정
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = ReviewAdapter(reviews)
        }
    }

    // 비로그인시 리뷰쪽에 적용할 blur 효과 설정
    private fun setBlurView() {
        // blur될 레이아웃의 루트 설정
        val rootView = binding.cvRecycler as ViewGroup
        val windowBackGround = requireActivity().window.decorView.background

        with(binding) {
            blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackGround)
                .setBlurRadius(2.5f)
                .setBlurAutoUpdate(true)
                .setOverlayColor(Color.parseColor("#78ffffff"))
        }

    }

}
