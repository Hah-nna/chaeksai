package com.jeong.sesac.sai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.sai.databinding.FragmentHomeBinding
import com.jeong.sesac.sai.recycler.recentlyFoundNotesRecycler.RecentlyFoundNotesAdapter
import com.jeong.sesac.sai.recycler.weeklynotes.WeeklyNotesAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.WeeklyNoteMockData

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var weeklyNoteAdapter: WeeklyNotesAdapter
    private lateinit var recentlyFoundAdapter : RecentlyFoundNotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weeklyNoteAdapter = WeeklyNotesAdapter { weeklyNote ->
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentWeeklyNotes(weeklyNote)
            findNavController().navigate(action)

        }

        recentlyFoundAdapter = RecentlyFoundNotesAdapter { foundNote ->
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentRecentlyFoundNotes()
            findNavController().navigate(action)

        }

        with(binding) {
            rvWeeklyNotes.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = this@HomeFragment.weeklyNoteAdapter
            }

            rvRecentlyFoundNotes.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = this@HomeFragment.recentlyFoundAdapter
            }
        }
        weeklyNoteAdapter.submitList(WeeklyNoteMockData.notesList)
        recentlyFoundAdapter.submitList(WeeklyNoteMockData.notesList)
    }

}