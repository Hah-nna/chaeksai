package com.jeong.sesac.sai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentSearchRegisterBinding
import com.jeong.sesac.sai.util.BaseFragment

class MapSearchRegisterFragment : BaseFragment<FragmentSearchRegisterBinding>(FragmentSearchRegisterBinding::inflate) {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            registerBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentSearchRegister_to_fragmentRegisterDetail)
            }

            searchBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentSearchRegister_to_fragmentSearchList)
            }
        }
    }
}