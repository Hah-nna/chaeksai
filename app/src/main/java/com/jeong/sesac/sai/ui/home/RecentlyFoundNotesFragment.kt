package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentRecentlyFoundNotesBinding
import com.jeong.sesac.sai.recycler.recentlyFoundNote.RecentlyFoundNotePagerAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.appcompat.navigationClicks

class RecentlyFoundNotesFragment : BaseFragment<FragmentRecentlyFoundNotesBinding>(FragmentRecentlyFoundNotesBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.toolbar.toolbarView) {
            setTitle(R.string.RECENTLY_FOUND_NOTES_TOOLBAR_TITLE)
            // 툴바를 클릭했을 때 뒤로가기
            navigationClicks().throttleFirst(throttleTime).onEach {
            findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }

        // 물리적인 뒤로가기 키를 누르면 뒤로 가기
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        binding.viewPager.adapter = RecentlyFoundNotePagerAdapter(this@RecentlyFoundNotesFragment)

        TabLayoutMediator(binding.buttonGroup, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "최신순"
                1 -> "좋아요높은순"
                else -> "좋아요높은순"
            }
        }.attach()

    }



}