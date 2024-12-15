package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.sai.databinding.FragmentRecentlyFoundNotesBinding
import com.jeong.sesac.sai.recycler.gridRecycler.GridNotesAdapter
import com.jeong.sesac.sai.recycler.gridRecycler.GridRecyclerDecoration
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.RECENTLY_FOUND_NOTES_TOOLBAR_TITLE
import com.jeong.sesac.sai.util.WeeklyNoteMockData

class RecentlyFoundNotesFragment : BaseFragment<FragmentRecentlyFoundNotesBinding>(FragmentRecentlyFoundNotesBinding::inflate) {
    private lateinit var recentlyFoundAdapter : GridNotesAdapter

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
        binding.toolbar.toolbarView.title = RECENTLY_FOUND_NOTES_TOOLBAR_TITLE

        binding.toolbar.toolbarView.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        recentlyFoundAdapter = GridNotesAdapter { foundNote ->
            val action = RecentlyFoundNotesFragmentDirections.actionFragmentRecentlyFoundNotesToDetail(foundNote)
            findNavController().navigate(action)
        }

        with(binding) {
            rvGridNotesList.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                addItemDecoration(GridRecyclerDecoration(2, 96))
                adapter = this@RecentlyFoundNotesFragment.recentlyFoundAdapter
            }
        }
        recentlyFoundAdapter.submitList(WeeklyNoteMockData.notesList)
    }



}