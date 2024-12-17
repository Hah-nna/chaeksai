package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentRecentlyFoundNotesDetailBinding
import com.jeong.sesac.sai.util.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.appcompat.navigationClicks
import ru.ldralighieri.corbind.view.clicks

class RecentlyFoundNotesDetailFragment : BaseFragment<FragmentRecentlyFoundNotesDetailBinding>(FragmentRecentlyFoundNotesDetailBinding::inflate){

    private val args : RecentlyFoundNotesDetailFragmentArgs by navArgs()
    private val noteDetail by lazy { args.recentlyFoundNoteDetail }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecentlyFoundNotesDetailBinding.inflate(layoutInflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding){
            toolbar.toolbarView.setTitle(R.string.RECENTLY_FOUND_NOTES_TOOLBAR_TITLE)
            includedNoteCv.cvNoteDetailImg.setImageResource(noteDetail.hint_img)
            includedNoteCv.iconBook.visibility = View.GONE

            btnGoToHome.clicks().onEach {
                val action = RecentlyFoundNotesDetailFragmentDirections.actionFragmentRecentlyFoundNotesDetailToFragmentHome()
                findNavController().navigate(action)
            }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .launchIn(viewLifecycleOwner.lifecycleScope)

            toolbar.toolbarView.navigationClicks().onEach {
            findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }



}