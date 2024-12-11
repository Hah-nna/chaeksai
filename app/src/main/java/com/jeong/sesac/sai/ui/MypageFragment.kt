package com.jeong.sesac.sai.ui

import ReviewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.data.Review
import com.jeong.sesac.sai.databinding.FragmentMypageBinding
import com.jeong.sesac.sai.util.BaseFragment

class MypageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 더미 데이터 생성
        val reviews = listOf(
            Review(R.drawable.ic_profile, "User1", "Great app!"),
            Review(R.drawable.ic_profile, "User2", "Loved it!"),
            Review(R.drawable.ic_profile, "User3", "Needs improvement.")
        )

        // 어댑터 설정
        val adapter = ReviewAdapter(reviews)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter


        with(binding) {
            // 설정 버튼 클릭 이벤트
            myPageToSettingsBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_myPage_to_fragment_settings)
            }
            reviewMoreBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_myPage_to_fragmentReceivedReviews)
            }
        }
    }
}