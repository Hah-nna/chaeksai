package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.sai.databinding.ItemTabRecyclerBinding
import com.jeong.sesac.sai.recycler.GridDecoration
import com.jeong.sesac.sai.recycler.recentlyFoundNote.RecentlyFoundNoteAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.WeeklyNoteMockData

class RecentlyFoundNotesLIst : BaseFragment<ItemTabRecyclerBinding>(ItemTabRecyclerBinding::inflate) {

    private lateinit var recentlyFoundAdapter : RecentlyFoundNoteAdapter

    companion object {
        fun getInstance(position: Int) =
            RecentlyFoundNotesLIst().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recentlyFoundAdapter = RecentlyFoundNoteAdapter { foundNote ->
            val action = RecentlyFoundNotesFragmentDirections.actionFragmentRecentlyFoundNotesToDetail(foundNote)
            findNavController().navigate(action)
        }

        with(binding) {
           rvContainer.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                addItemDecoration(GridDecoration(requireContext()))
                adapter = this@RecentlyFoundNotesLIst.recentlyFoundAdapter
            }
        }

        val filteredList = when(arguments?.getInt(("position")) ?: 0) {
            0 -> WeeklyNoteMockData.notesList.sortedByDescending { it.date }
            1 -> WeeklyNoteMockData.notesList.sortedByDescending { it.likes }
            else -> WeeklyNoteMockData.notesList.sortedBy { it.likes }
        }
        recentlyFoundAdapter.submitList(filteredList)
    }
}