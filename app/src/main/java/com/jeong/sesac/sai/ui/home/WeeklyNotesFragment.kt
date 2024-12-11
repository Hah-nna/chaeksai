package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.sai.databinding.FragmentWeeklynotesBinding
import com.jeong.sesac.sai.recycler.gridRecycler.GridRecyclerDecoration
import com.jeong.sesac.sai.recycler.horizontalRecycler.HorizontalNotesAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.WeeklyNoteMockData

class WeeklyNotesFragment :
    BaseFragment<FragmentWeeklynotesBinding>(FragmentWeeklynotesBinding::inflate) {

    private lateinit var weeklyNoteAdapter: HorizontalNotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeeklynotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        weeklyNoteAdapter = HorizontalNotesAdapter { weeklyDetailNote ->
            val action = WeeklyNotesFragmentDirections.actionFragmentWeeklyNotesToDetail(weeklyDetailNote)
            findNavController().navigate(action)
        }

        with(binding) {
           rvGridNotesList.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                addItemDecoration(GridRecyclerDecoration(2, 96))
                adapter = this@WeeklyNotesFragment.weeklyNoteAdapter
            }
        }

    weeklyNoteAdapter.submitList(WeeklyNoteMockData.notesList)
    }
}