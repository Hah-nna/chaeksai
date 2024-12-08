package com.jeong.sesac.sai.ui.searchRegister.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentHomeBinding
import com.jeong.sesac.sai.databinding.FragmentRegisterDetailBinding
import com.jeong.sesac.sai.util.BaseFragment

class RegisterDetailFragment : BaseFragment<FragmentRegisterDetailBinding>(FragmentRegisterDetailBinding::inflate){


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            with(binding) {
                registerDetailBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentRegisterDetail_to_fragmentRegisterConfirmation)
                }
            }

    }
}