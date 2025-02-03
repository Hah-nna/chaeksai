package com.jeong.sesac.sai.ui.libraryMap.noteList

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil3.load
import coil3.request.crossfade
import coil3.size.Scale
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.databinding.FragmentLibraryNoteDetailBinding
import com.jeong.sesac.sai.recycler.comment.CommentAdapter
import com.jeong.sesac.sai.util.BaseFragment
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
    private val viewModel: NoteListViewModel by activityViewModels<NoteListViewModel> { appViewModelFactory }

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
           viewModel.selectedNoteState.collectLatest { note ->
               note?.let { bindData(it) }
           }
        }
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
//           tvCommentCount.text = "댓글 : ${args.libraryNote.comments.size}개"


//       commentAdapter.submitList(args.libraryNote.comments)
       }
    }
}