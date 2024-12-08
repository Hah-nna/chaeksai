package com.jeong.sesac.sai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentHomeBinding
import com.jeong.sesac.sai.util.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            homeToWeelyNotesBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentHome_to_fragmentWeeklyNotes)
            }

            homeToRecentlyFoundBtn.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentHome_to_fragmentRecentlyFoundNotes)
            }
        }
    }

}