package com.jeong.sesac.sai.ui.searchRegister.search

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.sai.databinding.ItemTabRecyclerBinding
import com.jeong.sesac.sai.recycler.GridDecoration
import com.jeong.sesac.sai.recycler.recentlyFoundNote.RecentlyFoundNoteAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.WeeklyNoteMockData

class SearchListFragment : BaseFragment<ItemTabRecyclerBinding>(ItemTabRecyclerBinding::inflate) {
    private lateinit var searchListAdapter : RecentlyFoundNoteAdapter

    companion object {
        fun getInstance(position: Int) =
            SearchListFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchListAdapter = RecentlyFoundNoteAdapter { findNoteInfo ->
            val action =
                SearchFragmentDirections.actionFragmentSearchToFragmentSearchNoteDetail(findNoteInfo)
            findNavController().navigate(action)
        }

        with(binding) {
            rvContainer.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                addItemDecoration(GridDecoration(requireContext()))
                adapter = this@SearchListFragment.searchListAdapter
            }
        }

        val filteredList = when(arguments?.getInt(("position")) ?: 0) {
            0 -> WeeklyNoteMockData.notesList.sortedByDescending { it.date }
            1 -> WeeklyNoteMockData.notesList.sortedByDescending { it.likes }
            else -> WeeklyNoteMockData.notesList.sortedBy { it.likes }
        }

        searchListAdapter.submitList(filteredList)
    }
}