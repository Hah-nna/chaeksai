package com.jeong.sesac.sai.ui.found

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentFoundNoteDetailBinding
import com.jeong.sesac.sai.util.BaseFragment

class FoundNoteDetailFragment: BaseFragment<FragmentFoundNoteDetailBinding>(FragmentFoundNoteDetailBinding::inflate) {


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
            foundNoteDetailBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentFoundNoteDetail_to_fragmentWriteReview)
            }


        }
    }
}