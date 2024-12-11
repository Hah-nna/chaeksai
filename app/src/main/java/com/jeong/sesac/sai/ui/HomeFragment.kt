package com.jeong.sesac.sai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.sai.databinding.FragmentHomeBinding
import com.jeong.sesac.sai.recycler.gridRecycler.GridNotesAdapter
import com.jeong.sesac.sai.recycler.horizontalRecycler.HorizontalNotesAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.WeeklyNoteMockData

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var weeklyNoteAdapter: HorizontalNotesAdapter
    private lateinit var recentlyFoundAdapter : GridNotesAdapter

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

        weeklyNoteAdapter = HorizontalNotesAdapter { weeklyNote ->
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentWeeklyNoteDetail(weeklyNote)
            findNavController().navigate(action)
        }

        recentlyFoundAdapter = GridNotesAdapter { foundNote ->
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentRecentlyFoundNotesDetail(foundNote)
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

            tvWeeklyNotesMore.setOnClickListener {
                val action = HomeFragmentDirections.actionFragmentHomeToFragmentWeeklyNotes(WeeklyNoteMockData.notesList.first()
                )
                findNavController().navigate(action)
            }

            tvRecentlyFoundNotesMore.setOnClickListener {
                val action = HomeFragmentDirections.actionFragmentHomeToFragmentRecentlyFoundNotes()
                findNavController().navigate(action)
            }
        }
        weeklyNoteAdapter.submitList(WeeklyNoteMockData.notesList)
        recentlyFoundAdapter.submitList(WeeklyNoteMockData.notesList)
    }

}