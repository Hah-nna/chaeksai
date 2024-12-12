package com.jeong.sesac.sai.ui.found

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentWriteReviewBinding
import com.jeong.sesac.sai.util.BaseFragment

class WriteReviewFragment : BaseFragment<FragmentWriteReviewBinding> (FragmentWriteReviewBinding::inflate) {
    private val args : WriteReviewFragmentArgs by navArgs()

     override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWriteReviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var content = "ddsadsadsadsdsasa"

        super.onViewCreated(view, savedInstanceState)
            with(binding) {
                btnCompletedReview.setOnClickListener {
                   val action = WriteReviewFragmentDirections.actionFragmentWriteReviewToFragmentReviewCompleted("1234", "1", content)
                    findNavController().navigate(action)
                }
            }
    }
}