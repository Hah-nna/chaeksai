package com.jeong.sesac.sai.ui

import ReviewAdapter
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.data.Review
import com.jeong.sesac.sai.databinding.FragmentMypageBinding
import com.jeong.sesac.sai.util.BaseFragment
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class MypageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            // Corbind를 사용하여 설정 버튼 클릭 이벤트 처리
            lifecycleScope.launch {
                myPageToSettingsBtn.clicks().collect {
                    findNavController().navigate(R.id.action_fragment_myPage_to_fragment_settings)
                }
            }

            // Corbind를 사용하여 리뷰 더보기 버튼 클릭 이벤트 처리
            lifecycleScope.launch {
                reviewMoreBtn.clicks().collect {
                    findNavController().navigate(
                        R.id.action_fragment_myPage_to_fragmentReceivedReviews
                    )
                }
            }
        }
    }
}