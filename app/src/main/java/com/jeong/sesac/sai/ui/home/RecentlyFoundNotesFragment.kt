package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentRecentlyFoundNotesBinding
import com.jeong.sesac.sai.util.BaseFragment

class RecentlyFoundNotesFragment : BaseFragment<FragmentRecentlyFoundNotesBinding>(FragmentRecentlyFoundNotesBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecentlyFoundNotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recentlyFoundNotesToNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentRecentlyFoundNotes_to_detail)
            }
        }
    }



}