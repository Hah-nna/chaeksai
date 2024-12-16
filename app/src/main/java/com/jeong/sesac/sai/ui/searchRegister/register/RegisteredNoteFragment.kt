package com.jeong.sesac.sai.ui.searchRegister.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.databinding.FragmentRegisteredNoteBinding
import com.jeong.sesac.sai.util.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class RegisteredNoteFragment : BaseFragment<FragmentRegisteredNoteBinding>(FragmentRegisteredNoteBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisteredNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            includeCv.iconBook.visibility = View.VISIBLE
            includeCv.tvBookTitle.visibility = View.VISIBLE

            btnConfirm.clicks().onEach {
                val action = RegisteredNoteFragmentDirections.actionFragmentRegisteredNoteToFragmentHome()
                findNavController().navigate(action)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

}