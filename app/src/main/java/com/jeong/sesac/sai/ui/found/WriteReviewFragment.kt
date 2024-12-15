package com.jeong.sesac.sai.ui.found

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentWriteReviewBinding
import com.jeong.sesac.sai.util.BACK_TOOLBAR_TITLE
import com.jeong.sesac.sai.util.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

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
            toolbar.toolbarView.title = BACK_TOOLBAR_TITLE
            btnCompletedReview.clicks().onEach {
                val action = WriteReviewFragmentDirections.actionFragmentWriteReviewToFragmentReviewCompleted("1234", "1", content)
                findNavController().navigate(action)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            toolbar.toolbarView.clicks().onEach {
                findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }
}