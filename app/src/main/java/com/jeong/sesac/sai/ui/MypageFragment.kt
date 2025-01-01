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
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
                    val action = MypageFragmentDirections.actionFragmentMyPageToFragmentReceivedReviews()
                    findNavController().navigate(action)
                }.launchIn(lifecycleScope)
            }
        }
    }
