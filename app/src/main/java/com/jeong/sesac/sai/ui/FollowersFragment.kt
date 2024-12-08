package com.jeong.sesac.sai.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentFollowersBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.viewmodel.FollowersViewModel

/** writer: 정지영
 *
 * Followers fragment
 * 팔로우 프래그먼트
 *
 * 마이페이지 -> 설정 -> 개인정보 관리 -> 팔로우
 */
class FollowersFragment :
    BaseFragment<FragmentFollowersBinding>(FragmentFollowersBinding::inflate) {

    companion object {
        fun getInstance() = FollowersFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentFollowersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}