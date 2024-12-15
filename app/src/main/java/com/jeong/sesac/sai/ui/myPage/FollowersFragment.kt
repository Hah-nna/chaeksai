package com.jeong.sesac.sai.ui.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.data.Follower
import com.jeong.sesac.sai.databinding.FragmentFollowersBinding
import com.jeong.sesac.sai.ui.adapter.FollowerAdapter
import com.jeong.sesac.sai.util.BaseFragment

/** writer: 정지영
 *
 * Followers fragment
 * 팔로우 프래그먼트
 *
 * 마이페이지 -> 설정 -> 개인정보 관리 -> 팔로우
 */
class FollowersFragment :
    BaseFragment<FragmentFollowersBinding>(FragmentFollowersBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentFollowersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar 뒤로가기
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        // 더미 데이터 생성
        val followers = listOf(
            Follower(R.drawable.ic_profile, "John Doe"),
            Follower(R.drawable.ic_profile, "Jane Smith"),
            Follower(R.drawable.ic_profile, "Alice Brown")
        )

        // RecyclerView 설정
        val adapter = FollowerAdapter(followers)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }
}