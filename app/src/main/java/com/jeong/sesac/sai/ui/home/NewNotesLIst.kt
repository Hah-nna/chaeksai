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
import com.jeong.sesac.sai.databinding.ItemTabRecyclerBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.recycler.GridDecoration
import com.jeong.sesac.sai.recycler.newNote.NewNoteAdapter
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.CustomSnackBar.Companion.snackBar
import com.jeong.sesac.sai.viewmodel.NoteListViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewNotesLIst : BaseFragment<ItemTabRecyclerBinding>(ItemTabRecyclerBinding::inflate) {

    private lateinit var recentlyCreatedAdapter : NewNoteAdapter
    private val viewModel: NoteListViewModel by activityViewModels<NoteListViewModel> { appViewModelFactory }
    private lateinit var preference: AppPreferenceManager

    companion object {
        fun getInstance(position: Int) =
            NewNotesLIst().apply {
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

        recentlyCreatedAdapter = NewNoteAdapter { foundNote ->
            val action = NewNotesFragmentDirections.actionFragmentNewNotesToDetail(foundNote.id)
            findNavController().navigate(action)
        }

        with(binding) {
           rvContainer.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                addItemDecoration(GridDecoration(requireContext()))
                adapter = this@NewNotesLIst.recentlyCreatedAdapter
            }
        }

        val filteredType = when(arguments?.getInt(("position")) ?: 0) {
            0 -> NoteFilterType.ByCreatedAt(ascending = false)
            1 -> NoteFilterType.ByLikes(ascending = false)
            else -> NoteFilterType.ByLikes(ascending = true)
        }
        viewModel.getNoteList(filteredType, preference.userId)

        viewLifecycleOwner.lifecycleScope.launch {
                viewModel.noteListState.collectLatest { state ->
                    when(state) {
                        is UiState.Loading -> {
                            binding.progress.progressCircular.isVisible = true
                        }
                        is UiState.Success -> {
                            binding.progress.progressCircular.isVisible = false
                            recentlyCreatedAdapter.submitList(state.data)
                        }
                        is UiState.Error -> {
                            binding.progress.progressCircular.isVisible = false
                            snackBar(binding.root, requireContext(), state.error)
                        }
                }
            }
        }
    }
    }
