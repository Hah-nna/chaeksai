package com.jeong.sesac.sai.ui.myPage

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.data.Follower
import com.jeong.sesac.sai.databinding.FragmentFollowersBinding
import com.jeong.sesac.sai.ui.adapter.FollowerAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.FOLLOWER_TOOLBAR_TITLE
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.activity.backPresses

/** writer: 정지영
 *
 * Followers fragment
 * 팔로우 프래그먼트
 *
 * 마이페이지 -> 설정 -> 개인정보 관리 -> 팔로우
 */
class FollowersFragment :
    BaseFragment<FragmentFollowersBinding>(FragmentFollowersBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar 설정
        with(binding.toolbar.toolbarView) {
            title = FOLLOWER_TOOLBAR_TITLE

            // 뒤로가기 버튼 동작
            requireActivity().onBackPressedDispatcher.backPresses(viewLifecycleOwner)
                .onEach { findNavController().navigateUp() }
                .launchIn(lifecycleScope)
        }

        // 더미 데이터 생성
        val followers = mutableListOf(
            Follower(R.drawable.ic_profile, "John Doe", true),
            Follower(R.drawable.ic_profile, "Jane Smith", true),
            Follower(R.drawable.ic_profile, "Alice Brown", false)
        )

        // RecyclerView 설정
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = FollowerAdapter(followers, requireActivity())
        }
    }
}