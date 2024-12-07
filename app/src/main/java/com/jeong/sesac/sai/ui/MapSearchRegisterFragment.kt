package com.jeong.sesac.sai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeong.sesac.sai.databinding.FragmentSearchRegisterBinding
import com.jeong.sesac.sai.util.BaseFragment

class MapSearchRegisterFragment :
    BaseFragment<FragmentSearchRegisterBinding>(FragmentSearchRegisterBinding::inflate) {

    companion object {
        fun getInstance() = MapSearchRegisterFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentSearchRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}