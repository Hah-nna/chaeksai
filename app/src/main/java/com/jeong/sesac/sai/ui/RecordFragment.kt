package com.jeong.sesac.sai.ui

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.jeong.sesac.sai.databinding.FragmentTabLayoutBinding
import com.jeong.sesac.sai.recycler.Record.RecordPagerAdapter
import com.jeong.sesac.sai.util.BaseFragment

class RecordFragment : BaseFragment<FragmentTabLayoutBinding>(FragmentTabLayoutBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * ViewPager2에 RecordPagerAdapter 연결
         */
        binding.viewPager.adapter = RecordPagerAdapter(this@RecordFragment)

        /**
         * TabLayout과 ViewPager2를 TabLayoutMediator로 연결
         * */

        TabLayoutMediator(binding.buttonGroup, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "등록한쪽지"
                1 -> "찾은쪽지"
                else -> "찜한쪽지"
            }
        }.attach()
    }


}