package com.jeong.sesac.sai.ui.searchRegister.register

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentRegisterConfirmationBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.factory.viewModelFactory
import com.jeong.sesac.sai.viewmodel.RegisterNoteViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class RegisterConfirmationFragment : BaseFragment<FragmentRegisterConfirmationBinding>(FragmentRegisterConfirmationBinding::inflate) {

    private val registerNoteViewModel by activityViewModels<RegisterNoteViewModel>{ viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.toolbarView.setTitle(R.string.BACK_TOOLBAR_TITLE)
            includeCv.iconBook.visibility = View.VISIBLE
            includeCv.tvBookTitle.visibility = View.VISIBLE
            btnConfirm.clicks().throttleFirst(throttleTime).onEach {

                val action = RegisterConfirmationFragmentDirections.actionFragmentRegisterConfirmationToFragmentRegisteredNote()
                findNavController().navigate(action)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            toolbar.toolbarView.clicks().throttleFirst(throttleTime).onEach {
                findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }
}