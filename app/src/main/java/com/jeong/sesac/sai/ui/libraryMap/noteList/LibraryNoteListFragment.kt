package com.jeong.sesac.sai.ui.libraryMap.noteList

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.jeong.sesac.domain.model.NoteFilterType
import com.jeong.sesac.sai.databinding.FragmentLibraryNotesBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.recycler.libraryNote.LibraryNotesAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.viewmodel.NoteListViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class LibraryNoteListFragment() :
    BaseFragment<FragmentLibraryNotesBinding>(FragmentLibraryNotesBinding::inflate) {
    private lateinit var libraryNotesAdapter: LibraryNotesAdapter
    private val args: LibraryNoteListFragmentArgs by navArgs()
//    private lateinit var listPopupWindow : ListPopupWindow

    private val viewModel: NoteListViewModel by viewModels<NoteListViewModel> { appViewModelFactory }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("도서관이름", "${args.libraryName}")



        libraryNotesAdapter = LibraryNotesAdapter(lifecycleScope) { libraryNote ->
            val action =
                LibraryNoteListFragmentDirections.actionFragmentLibraryNotesListToFragmentLibraryNoteDetail(
                    libraryNote.id
                )
            findNavController().navigate(action)
        }

        binding.rvContainer.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
            adapter = libraryNotesAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getLibraryNotes(args.libraryName)
            viewModel.libraryNotesState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progress.progressCircular.isVisible = true
                    }

                    is UiState.Success -> {
                        with(binding) {
                            progress.progressCircular.isVisible = false
                            btnTitle.text = args.libraryName
                        }

                        if (state.data.isEmpty()) {
                            binding.rvContainer.isVisible = false
                            binding.tvEmptyNoteList.text = "아직 도서관에 쪽지가 없네요\n등록해주세요"
                        } else {
                            binding.rvContainer.isVisible = true
                            binding.tvEmptyNoteList.isVisible = false
                            libraryNotesAdapter.submitList(state.data)
                        }

                    }

                    is UiState.Error -> {
                        binding.progress.progressCircular.isVisible = false
                        // 스낵바주기
                    }

                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }
}