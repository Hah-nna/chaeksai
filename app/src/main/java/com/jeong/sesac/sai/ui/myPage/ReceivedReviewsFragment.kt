package com.jeong.sesac.sai.ui.myPage

import ReviewAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.data.Review
import com.jeong.sesac.sai.databinding.FragmentReceivedReviewsBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.factory.viewModelFactory
import com.jeong.sesac.sai.viewmodel.MyPageViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.activity.backPresses
import ru.ldralighieri.corbind.appcompat.navigationClicks

class ReceivedReviewsFragment :
    BaseFragment<FragmentReceivedReviewsBinding>(FragmentReceivedReviewsBinding::inflate) {

    private val myPageViewModel by activityViewModels<MyPageViewModel>{ viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            // 툴바 설정
            toolbar.toolbarView.apply {
                setTitle(R.string.RECEIVED_REVIEW_TOOLBAR_TITLE)
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

            // RecyclerView 설정
            reviewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            reviewsRecyclerView.adapter = ReviewAdapter(getDummyReviews())
        }
    }

    private fun getDummyReviews(): List<Review> {

        // 실제 데이터 로직으로 교체 필요
        return listOf(
            Review(R.drawable.ic_profile, "User1", "Great app!"),
            Review(R.drawable.ic_profile, "User2", "Loved it!"),
            Review(R.drawable.ic_profile, "User3", "Needs improvement.")
        )
    }
}