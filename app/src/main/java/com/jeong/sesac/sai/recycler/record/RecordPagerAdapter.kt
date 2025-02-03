package com.jeong.sesac.sai.recycler.record

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jeong.sesac.sai.ui.record.RecordListFragment

class RecordPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /** viewPager에서 보여줄 총 프래그먼트 수 */
    override fun getItemCount() = 2

    /**
     * 항목을 구성하는 프래그먼트 객체를 얻음
     * createFragment에서 반환하는 프래그먼트가 각 항목에 표시됨
     * 여기서는 RecordListFragment의 인스턴스 생성
     * */
    override fun createFragment(position: Int): Fragment {
       return RecordListFragment.getInstance(position)
    }

}