package com.jeong.sesac.sai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeong.sesac.sai.databinding.FragmentRecordBinding
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.util.BaseFragment

class RecordFragment : BaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recordToMyRegisteredNotesBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_record_to_fragment_my_registered_notes)
            }

            recordToCompletedFindsBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_record_to_fragment_completed_finds)
            }

            recordToBookmarkedNotesBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragment_record_to_fragment_bookmarkedNotes)
            }
        }
    }
}