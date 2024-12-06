package com.jeong.sesac.sai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeong.sesac.sai.databinding.FragmentHomeBinding
import com.jeong.sesac.sai.databinding.FragmentWeeklynotesBinding
import com.jeong.sesac.sai.util.BaseFragment

class WeeklyNotesFragment : BaseFragment<FragmentWeeklynotesBinding>(FragmentWeeklynotesBinding::inflate) {

    companion object {
        fun getInstance() = WeeklyNotesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeeklynotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}