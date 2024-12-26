package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentWeeklynotesBinding
import com.jeong.sesac.sai.recycler.weeklyNote.WeeklyPagerAdapter
import com.jeong.sesac.sai.util.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.appcompat.navigationClicks

class WeeklyNotesFragment :
    BaseFragment<FragmentWeeklynotesBinding>(FragmentWeeklynotesBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = WeeklyPagerAdapter(this@WeeklyNotesFragment)

        TabLayoutMediator(binding.buttonGroup, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "최신순"
                1 -> "좋아요높은순"
                else -> "좋아요높은순"
            }
        }.attach()

        with(binding.toolbar.toolbarView) {
            setTitle(R.string.WEEKLY_NOTES_TOOLBAR_TITLE)
            /**
             * https://github.com/LDRAlighieri/Corbind/blob/master/corbind/src/main/kotlin/ru/ldralighieri/corbind/widget/ToolbarNavigationClicks.kt
             * 여기를 참조해서 toolbar 이벤트 설정함
             * navigationClicks()로 만들어진 flow는 Toolbar.setNavigationOnClickListener를 사용한다고해서 사용함
             * */
            navigationClicks().onEach {
                findNavController().navigateUp()
                Log.d("weekly", "실행")
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                findNavController().navigateUp()
            }

        }
    }
}