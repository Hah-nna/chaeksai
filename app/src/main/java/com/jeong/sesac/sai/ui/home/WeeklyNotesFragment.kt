package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.databinding.FragmentWeeklynotesBinding
import com.jeong.sesac.sai.util.BaseFragment

class WeeklyNotesFragment :
    BaseFragment<FragmentWeeklynotesBinding>(FragmentWeeklynotesBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeeklynotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            weeklynotesToNextBtn.setOnClickListener {
            }
        }
    }
}