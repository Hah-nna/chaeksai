package com.jeong.sesac.sai.ui.libraryMap.noteList

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
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
    private lateinit var listPopupWindow : ListPopupWindow

    private val viewModel: NoteListViewModel by activityViewModels<NoteListViewModel> { appViewModelFactory }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        libraryNotesAdapter = LibraryNotesAdapter(lifecycleScope) { libraryNote ->
            val action =
                LibraryNoteListFragmentDirections.actionFragmentLibraryNotesListToFragmentLibraryNoteDetail(libraryNote.id)
            findNavController().navigate(action)
        }

        binding.rvContainer.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
            adapter = libraryNotesAdapter
        }

        listPopupWindow = ListPopupWindow(requireContext()).apply {
            anchorView = binding.btnTitle
            val items = listOf(args.libraryName, "다른 도서관")
            setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items))
            setOnItemClickListener { _, _, position, _ ->
                when(position) {
                    0 -> viewModel.getNoteList(NoteFilterType.ByLibrary(args.libraryName))
                    1 -> viewModel.getNoteList(NoteFilterType.ByLibrary(""))
                }
                dismiss()
            }
        }
        with(binding) {
            btnTitle.text = args.libraryName
            btnTitle.clicks().onEach {
                listPopupWindow.show()
            }.launchIn(lifecycleScope)
        }


        fetchNoteList()


    }

    private fun fetchNoteList() {
        lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.noteListState.collectLatest { state ->
                when(state) {
                    is UiState.Loading -> {
                        binding.progress.progressCircular.isVisible = true
                    }
                    is UiState.Success -> {
                        binding.progress.progressCircular.isVisible = false
                        if(state.data.isEmpty()) {
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
                        Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

        }
    }
}