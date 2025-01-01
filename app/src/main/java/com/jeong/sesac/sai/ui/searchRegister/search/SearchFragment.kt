package com.jeong.sesac.sai.ui.searchRegister.search

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentSearchBinding
import com.jeong.sesac.sai.recycler.search.SearchPagerAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.appcompat.navigationClicks

class SearchFragment : BaseFragment<FragmentSearchBinding> (FragmentSearchBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = SearchPagerAdapter(this@SearchFragment)

        TabLayoutMediator(binding.buttonGroup, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "최신순"
                1 -> "좋아요높은순"
                else -> "좋아요높은순"
            }
        }.attach()

        with(binding.toolbar.toolbarView) {
            setTitle(R.string.SEARCH_NOTES_TOOLBAR_TITLE)
            navigationClicks().onEach {
                findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }




    }
}