package com.jeong.sesac.sai.ui.found

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentReviewCompletedBinding
import com.jeong.sesac.sai.databinding.FragmentWriteReviewBinding
import com.jeong.sesac.sai.util.BaseFragment

class ReviewCompletedFragment : BaseFragment<FragmentReviewCompletedBinding>(FragmentReviewCompletedBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewCompletedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            goToHomeBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentReviewCompleted_to_fragmentHome)
            }

            registerNoteBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentReviewCompleted_to_fragmentSearchRegister)
            }

        }
    }
}