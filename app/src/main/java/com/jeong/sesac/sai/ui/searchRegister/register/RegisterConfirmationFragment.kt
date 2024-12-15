package com.jeong.sesac.sai.ui.searchRegister.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentRegisterConfirmationBinding
import com.jeong.sesac.sai.util.BACK_TOOLBAR_TITLE
import com.jeong.sesac.sai.util.BaseFragment

class RegisterConfirmationFragment : BaseFragment<FragmentRegisterConfirmationBinding>(FragmentRegisterConfirmationBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterConfirmationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.toolbarView.title = BACK_TOOLBAR_TITLE
            includeCv.iconBook.visibility = View.VISIBLE
            includeCv.tvBookTitle.visibility = View.VISIBLE
            btnConfirm.setOnClickListener {
                val action = RegisterConfirmationFragmentDirections.actionFragmentRegisterConfirmationToFragmentRegisteredNote()
                findNavController().navigate(action)
            }
        }
    }
}