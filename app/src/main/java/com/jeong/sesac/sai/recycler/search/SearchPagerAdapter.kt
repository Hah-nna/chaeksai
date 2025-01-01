package com.jeong.sesac.sai.recycler.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jeong.sesac.sai.ui.searchRegister.search.SearchListFragment

class SearchPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return SearchListFragment.getInstance(position)
    }
}