package com.jeong.sesac.sai.ui.found

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentFoundNoteDetailBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.GO_HOME_TOOLBAR_TITLE

class FoundNoteDetailFragment: BaseFragment<FragmentFoundNoteDetailBinding>(FragmentFoundNoteDetailBinding::inflate) {

    private val args : FoundNoteDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoundNoteDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.toolbarView.title = GO_HOME_TOOLBAR_TITLE
           btnWriteReview.setOnClickListener {
             val action = FoundNoteDetailFragmentDirections.actionFragmentFoundNoteDetailToFragmentWriteReview("1234", "1")
            findNavController().navigate(action)
           }

        }
    }
}