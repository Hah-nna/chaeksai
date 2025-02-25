package com.jeong.sesac.sai.ui.home

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.domain.model.NoteFilterType
import com.jeong.sesac.domain.model.SortOrder
import com.jeong.sesac.sai.databinding.ItemTabRecyclerBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.recycler.GridDecoration
import com.jeong.sesac.sai.recycler.weeklyNote.WeeklyNoteAdapter
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.CustomSnackBar
import com.jeong.sesac.sai.viewmodel.NoteListViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WeeklyNoteListFragment :
    BaseFragment<ItemTabRecyclerBinding>(ItemTabRecyclerBinding::inflate) {
    private lateinit var weeklyNoteAdapter: WeeklyNoteAdapter
    private val viewModel: NoteListViewModel by activityViewModels<NoteListViewModel> { appViewModelFactory }
    private lateinit var preference: AppPreferenceManager

    companion object {
        fun getInstance(position: Int) =
            WeeklyNoteListFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = AppPreferenceManager.getInstance(requireContext())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        weeklyNoteAdapter = WeeklyNoteAdapter { weeklyDetailNote ->
            val action =
                WeeklyNotesFragmentDirections.actionFragmentWeeklyNotesToDetail(weeklyDetailNote.id)
            findNavController().navigate(action)
        }

        with(binding) {
            rvContainer.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                addItemDecoration(GridDecoration(requireContext()))
                adapter = this@WeeklyNoteListFragment.weeklyNoteAdapter
            }
        }


        val filteredType = when (arguments?.getInt("position") ?: 0) {
            0 -> NoteFilterType.ThisWeek(SortOrder.LATEST)
            1 -> NoteFilterType.ThisWeek(SortOrder.LIKES_DESC)
            else -> NoteFilterType.ThisWeek(SortOrder.LIKES_ASC)
        }
        viewModel.getNoteList(filteredType, preference.userId)

        viewLifecycleOwner.lifecycleScope.launch {
                viewModel.noteListState.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progress.progressCircular.isVisible = true
                        }

                        is UiState.Success -> {
                            binding.progress.progressCircular.isVisible = false
                            weeklyNoteAdapter.submitList(state.data)
                        }

                        is UiState.Error -> {
                            binding.progress.progressCircular.isVisible = false
                            CustomSnackBar.snackBar(binding.root, requireContext(), state.error)                        }
                    }
                }
            }
    }
}







