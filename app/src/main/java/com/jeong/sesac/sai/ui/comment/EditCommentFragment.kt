package com.jeong.sesac.sai.ui.comment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentEditCommentBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.CommentViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class EditCommentFragment :
    BaseFragment<FragmentEditCommentBinding>(FragmentEditCommentBinding::inflate) {
    private val args: EditCommentFragmentArgs by navArgs()
    private val commentViewModel: CommentViewModel by activityViewModels<CommentViewModel> { appViewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            binding.toolbar.clicks().onEach {
                findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            etComment.setText(args.content)

            tvEdit.clicks().throttleFirst(throttleTime).onEach {
                val newContent = etComment.text.toString()
                Log.d("newComment", "$newContent")
                if (newContent.isNotEmpty()) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        commentViewModel.updateComment(
                            args.nickname,
                            args.noteId,
                            args.commentId,
                            newContent
                        )
                        commentViewModel.commentUpdateState.collectLatest { state ->
                            when (state) {
                                is UiState.Loading -> binding.progress.progressCircular.isVisible = true
                                is UiState.Success -> {
                                    binding.progress.progressCircular.isVisible = false
                                    UiState.Success(state.data)
                                    findNavController().navigateUp()
                                }
                                is UiState.Error -> {
                                    binding.progress.progressCircular.isVisible = false
                                    Log.e("업데이트 실패", state.error)
                                }
                            }
                        }
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

}