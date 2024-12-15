package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.sai.databinding.FragmentRecentlyFoundNotesBinding
import com.jeong.sesac.sai.recycler.gridRecycler.GridNotesAdapter
import com.jeong.sesac.sai.recycler.gridRecycler.GridRecyclerDecoration
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.RECENTLY_FOUND_NOTES_TOOLBAR_TITLE
import com.jeong.sesac.sai.util.WeeklyNoteMockData
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.appcompat.navigationClicks

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

        with(binding.toolbar.toolbarView) {
            title = RECENTLY_FOUND_NOTES_TOOLBAR_TITLE
            // 툴바를 클릭했을 때 뒤로가기
            navigationClicks().onEach {
            findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }

        // 물리적인 뒤로가기 키를 누르면 뒤로 가기
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