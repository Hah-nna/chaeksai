package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentRecentlyFoundNotesDetailBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.RECENTLY_FOUND_NOTES_TOOLBAR_TITLE

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
            toolbar.toolbarView.title = RECENTLY_FOUND_NOTES_TOOLBAR_TITLE
            includedNoteCv.cvNoteDetailImg.setImageResource(noteDetail.hint_img)
            includedNoteCv.iconBook.visibility = View.GONE
            btnGoToHome.setOnClickListener {
                val action = RecentlyFoundNotesDetailFragmentDirections.actionFragmentRecentlyFoundNotesDetailToFragmentHome()
                findNavController().navigate(action)
            }
        }



        binding.toolbar.toolbarView.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }



}