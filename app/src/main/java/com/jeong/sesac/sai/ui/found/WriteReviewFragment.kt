package com.jeong.sesac.sai.ui.found

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentWriteReviewBinding
import com.jeong.sesac.sai.util.BaseFragment

class WriteReviewFragment : BaseFragment<FragmentWriteReviewBinding> (FragmentWriteReviewBinding::inflate) {

     override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWriteReviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            writeReviewBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentWriteReview_to_fragmentReviewCompleted)
            }

        }
    }
}