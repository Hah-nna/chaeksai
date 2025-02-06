package com.jeong.sesac.sai.ui.libraryMap.noteList

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil3.load
import coil3.request.crossfade
import coil3.size.Scale
import com.jeong.sesac.feature.model.Comment
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.databinding.FragmentLibraryNoteDetailBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.recycler.comment.CommentAdapter
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.CommentViewModel
import com.jeong.sesac.sai.viewmodel.NoteListViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class LibraryNoteDetailFragment : BaseFragment<FragmentLibraryNoteDetailBinding>(FragmentLibraryNoteDetailBinding::inflate){
    private lateinit var commentAdapter : CommentAdapter
    private val args: LibraryNoteDetailFragmentArgs by navArgs()
    private lateinit var preference : AppPreferenceManager

    private val viewModel: NoteListViewModel by activityViewModels<NoteListViewModel> { appViewModelFactory }
    private val commentViewModel : CommentViewModel by viewModels<CommentViewModel> { appViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = AppPreferenceManager.getInstance(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectNote(args.noteId)
        Log.d("noteId", "${args.noteId}")

        commentAdapter = CommentAdapter { comment ->
            // 꾹 누르면 menu (신고) 나오게 하기
        }
        binding.rvComments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentAdapter
        }

        binding.toolbar.toolbarView.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        binding.toolbar.toolbarView.clicks().onEach {
            findNavController().navigateUp()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
           viewModel.selectedNoteState.collectLatest { state ->

               when (state) {
                   is UiState.Loading -> binding.progress.progressCircular.isVisible =
                       true
                   is UiState.Success -> {
                       binding.progress.progressCircular.isVisible = false
                       state.let { it.data?.let { it1 -> bindData(it1) } }
                   }

                   is UiState.Error -> {
                       binding.progress.progressCircular.isVisible = false
                       Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT)
                   }
               }
           }
        }

        registerComment()
        getCommentList()
    }

    private fun getCommentList() {
        viewLifecycleOwner.lifecycleScope.launch {
                commentViewModel.getComments(preference.nickName, args.noteId)

                commentViewModel.commentListState.collectLatest { state ->
                    when(state) {
                        is UiState.Loading -> {
                            binding.progress.progressCircular.isVisible = true
                            binding.tvNoComment.isVisible = false
                        }
                        is UiState.Success -> {
                            binding.progress.progressCircular.isVisible = false
                            commentAdapter.submitList(state.data)

                            binding.tvNoComment.isVisible = state.data.isEmpty()
                            binding.tvCommentCount.text = "댓글 : ${state.data.size}개"
                        }
                        is UiState.Error -> {
                            binding.progress.progressCircular.isVisible = false
                            binding.tvNoComment.isVisible = true
                        }
                }
            }
        }
    }

    private fun registerComment() {
        binding.btnRegisterComment.clicks().throttleFirst(throttleTime).onEach {
            val content = binding.tvComment.text.toString()


            if(content.isNotEmpty()) {
               val comment = Comment(
                   id = "",
                   userId = "",
                   noteId = args.noteId,
                   content = content,
                   createdAt = System.currentTimeMillis()
               )

                commentViewModel.createComment(preference.nickName, args.noteId, comment)
                binding.tvComment.text?.clear()
            }

            commentViewModel.commentState.collectLatest {state ->
                when(state) {
                    is UiState.Loading -> binding.progress.progressCircular.isVisible = true
                    is UiState.Success -> {
                        binding.progress.progressCircular.isVisible = false
                    }
                    is UiState.Error ->  {
                        binding.progress.progressCircular.isVisible = false
                        Toast.makeText(requireContext(), "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }



    private fun bindData(note: NoteWithUser) {
       with(binding) {
           ivNote.load(note.image) {
               crossfade(true)
               scale(Scale.FILL)
           }
           ivProfile.load(note.userInfo.profile) {
               crossfade(true)
               scale(Scale.FILL)
           }
           tvNickname.text = note.userInfo.nickName
           tvTitle.text = note.title
           tvLibraryName.text = note.libraryName
           tvTime.text = note.createdAt.toString()
           tvContent.text = note.content

       }
    }
}