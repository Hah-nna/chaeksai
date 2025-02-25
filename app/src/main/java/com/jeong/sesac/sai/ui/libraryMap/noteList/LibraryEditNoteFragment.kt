package com.jeong.sesac.sai.ui.libraryMap.noteList

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil3.load
import coil3.request.crossfade
import coil3.size.Scale
import com.jeong.sesac.feature.model.Note
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.databinding.FragmentLibraryMapWriteNoteBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.NoteViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class LibraryEditNoteFragment : BaseFragment<FragmentLibraryMapWriteNoteBinding>(FragmentLibraryMapWriteNoteBinding::inflate) {

    private val args: LibraryEditNoteFragmentArgs by navArgs()
    private val viewModel: NoteViewModel by activityViewModels<NoteViewModel> { appViewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvWelcomeTitle.text = "내용 수정"
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedNoteState.collectLatest { state ->
                when(state) {
                    is UiState.Loading -> binding.progress.progressCircular.isVisible = true
                    is UiState.Success -> {
                        binding.progress.progressCircular.isVisible = false
                        bindData(state.data)
                    }
                    is UiState.Error -> {
                        binding.progress.progressCircular.isVisible = false
                        Log.e("노트 디테일 내용 수정 error", "${state.error}")
                    }
                }
            }
        }

        binding.btnComplete.clicks().throttleFirst(throttleTime).onEach {
            val updateNote = Note(
                id = args.noteId,
                title = binding.etvTitle.text.toString(),
                content = binding.tvNoteContent.text.toString(),
                userId = "",
                libraryName = "",
                likes = emptyList()
            )
            viewModel.updateNote(args.noteId, updateNote)

            viewModel.fetchNoteState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> binding.progress.progressCircular.isVisible = true
                    is UiState.Success -> {
                        binding.progress.progressCircular.isVisible = false
                        findNavController().navigateUp()
                    }

                    is UiState.Error -> {
                        binding.progress.progressCircular.isVisible = false
                        Toast.makeText(requireContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun bindData(data: NoteWithUser) {
        with(binding) {
            ivImg.load(data.image) {
                crossfade(true)
                scale(Scale.FILL)
            }
            etvTitle.setText(data.title)
            tvNoteContent.setText(data.content)
            icUploadImg.visibility = View.INVISIBLE
            btnComplete.text = "완료"
        }
    }
}