package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentWeeklyNoteDetailBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.WEEKLY_NOTES_TOOLBAR_TITLE

class WeeklyNotesDetailFragment : BaseFragment<FragmentWeeklyNoteDetailBinding>(FragmentWeeklyNoteDetailBinding::inflate) {

    private val args : WeeklyNotesDetailFragmentArgs by navArgs()
    private val noteDetail by lazy { args.weeklyNoteDetail }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeeklyNoteDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.toolbarView.title = WEEKLY_NOTES_TOOLBAR_TITLE
            includedNoteCv.iconBook.visibility = View.GONE
            includedNoteCv.cvNoteDetailImg.setImageResource(noteDetail.hint_img)
            btnBarcodeScanner.setOnClickListener {
                val action = WeeklyNotesDetailFragmentDirections.actionFragmentWeeklyDetailNotesToFragmentBarcodeScanner(
                    weeklyNoteDetail = noteDetail,
                    findNoteInfo = noteDetail
                )
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