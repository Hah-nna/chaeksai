package com.jeong.sesac.sai.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jeong.sesac.sai.ui.record.GridRecyclerViewFragment

/**
 * RecordPagerAdapter
 * - ViewPager2와 함께 사용되는 Adapter로, 각 페이지에 대해 Fragment를 생성합니다.
 *
 * @param fragment 부모 Fragment. ViewPager2와 연결됩니다.
 */
class RecordPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /**
     * 페이지 수를 반환
     * - 이 경우 총 3개의 페이지를 사용합니다.
     */
    override fun getItemCount(): Int = 3

    /**
     * 주어진 위치에 해당하는 Fragment를 생성
     * - GridRecyclerViewFragment의 newInstance 메서드를 호출하여 생성합니다.
     *
     * @param position 페이지 위치 (0, 1, 2)
     * @return 생성된 Fragment
     */
    override fun createFragment(position: Int): Fragment {
        return GridRecyclerViewFragment.newInstance(position)
    }
}