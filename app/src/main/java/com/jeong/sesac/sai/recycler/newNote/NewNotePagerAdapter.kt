package com.jeong.sesac.sai.recycler.newNote

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jeong.sesac.sai.ui.home.NewNotesLIst

class NewNotePagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment){

    override fun getItemCount():Int = 3

    override fun createFragment(position: Int): Fragment {
        return NewNotesLIst.getInstance(position)
    }
}