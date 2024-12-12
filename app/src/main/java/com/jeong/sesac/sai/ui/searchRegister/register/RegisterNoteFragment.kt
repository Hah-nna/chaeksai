package com.jeong.sesac.sai.ui.searchRegister.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.databinding.FragmentRegisterNoteBinding
import com.jeong.sesac.sai.util.BaseFragment

class RegisterNoteFragment : BaseFragment<FragmentRegisterNoteBinding>(FragmentRegisterNoteBinding::inflate) {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val content = "dasdsadasdsad"
        with(binding) {
            btnRegisterHint.setOnClickListener {
                val action = RegisterNoteFragmentDirections.actionFragmentRegisterNoteToFragmentRegisterDetail("청량리도서관", content)
                findNavController().navigate(action)
            }
        }
    }
}