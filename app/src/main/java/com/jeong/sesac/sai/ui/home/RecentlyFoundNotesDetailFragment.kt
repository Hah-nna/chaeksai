package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeong.sesac.sai.databinding.FragmentRecentlyFoundNotesDetailBinding
import com.jeong.sesac.sai.util.BaseFragment

class RecentlyFoundNotesDetailFragment : BaseFragment<FragmentRecentlyFoundNotesDetailBinding>(FragmentRecentlyFoundNotesDetailBinding::inflate){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecentlyFoundNotesDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }



}