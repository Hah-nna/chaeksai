package com.jeong.sesac.sai.ui.searchRegister.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.sai.databinding.FragmentSearchListBinding
import com.jeong.sesac.sai.recycler.gridRecycler.GridNotesAdapter
import com.jeong.sesac.sai.recycler.gridRecycler.GridRecyclerDecoration
import com.jeong.sesac.sai.ui.home.RecentlyFoundNotesFragmentDirections
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.SEARCH_NOTES_TOOLBAR_TITLE
import com.jeong.sesac.sai.util.WeeklyNoteMockData

class SearchListFragment : BaseFragment<FragmentSearchListBinding> (FragmentSearchListBinding::inflate) {
    private lateinit var searchListAdapter : GridNotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.toolbar.toolbarView) {
            title = SEARCH_NOTES_TOOLBAR_TITLE
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        searchListAdapter = GridNotesAdapter { findNoteInfo ->
            val action =
                SearchListFragmentDirections.actionFragmentSearchListToFragmentSearchNoteDetail(findNoteInfo)
            findNavController().navigate(action)
        }

        with(binding) {
            rvGridNotesList.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                addItemDecoration(GridRecyclerDecoration(2, 96))
                adapter = this@SearchListFragment.searchListAdapter
            }
        }
        searchListAdapter.submitList(WeeklyNoteMockData.notesList)


    }
}