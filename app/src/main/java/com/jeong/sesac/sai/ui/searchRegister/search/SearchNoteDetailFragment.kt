package com.jeong.sesac.sai.ui.searchRegister.search

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentSearchNoteDetailBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class SearchNoteDetailFragment : BaseFragment<FragmentSearchNoteDetailBinding>(FragmentSearchNoteDetailBinding::inflate){

    private val args : SearchNoteDetailFragmentArgs by navArgs()
    private val noteInfo by lazy { args.findNoteInfo }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.toolbarView.setTitle(R.string.SEARCH_NOTES_TOOLBAR_TITLE)
            btnBarcodeScan.clicks().throttleFirst(throttleTime).onEach {
                val action = SearchNoteDetailFragmentDirections
                    .actionFragmentSearchNoteDetailToFragmentBarcodeScanner(noteInfo)
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
