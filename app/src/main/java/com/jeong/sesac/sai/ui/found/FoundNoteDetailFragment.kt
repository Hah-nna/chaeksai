package com.jeong.sesac.sai.ui.found

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentFoundNoteDetailBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

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
            toolbar.toolbarView.setTitle(R.string.GO_HOME_TOOLBAR_TITLE)
            btnWriteReview.clicks().throttleFirst(throttleTime).onEach {
                val action = FoundNoteDetailFragmentDirections.actionFragmentFoundNoteDetailToFragmentWriteReview("1234", "1")
                findNavController().navigate(action)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            toolbar.toolbarView.clicks().throttleFirst(throttleTime).onEach {
                // 이 부분이야
                val action = FoundNoteDetailFragmentDirections.actionFragmentFoundNoteDetailToFragmentHome()
                findNavController().navigate(action)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }
}