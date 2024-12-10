package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.sai.databinding.FragmentRecentlyFoundNotesBinding
import com.jeong.sesac.sai.recycler.recentlyFoundNotesRecycler.RecentlyFoundNotesAdapter
import com.jeong.sesac.sai.recycler.recentlyFoundNotesRecycler.RecyclerDecoration
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.WeeklyNoteMockData

class RecentlyFoundNotesFragment : BaseFragment<FragmentRecentlyFoundNotesBinding>(FragmentRecentlyFoundNotesBinding::inflate) {
    private lateinit var recentlyFoundAdapter : RecentlyFoundNotesAdapter

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

        recentlyFoundAdapter = RecentlyFoundNotesAdapter { foundNote ->
            val action = RecentlyFoundNotesFragmentDirections.actionFragmentRecentlyFoundNotesToDetail(foundNote)
            findNavController().navigate(action)
        }

        with(binding) {
            rvRecentlyFoundNotesList.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                addItemDecoration(RecyclerDecoration(2, 96))
                adapter = this@RecentlyFoundNotesFragment.recentlyFoundAdapter
            }
        }
        recentlyFoundAdapter.submitList(WeeklyNoteMockData.notesList)
    }



}