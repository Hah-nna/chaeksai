package com.jeong.sesac.sai.ui.libraryMap.bookReview

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentEditBookReviewBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.CustomSnackBar
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.BookViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class BookReviewEditFragment :
    BaseFragment<FragmentEditBookReviewBinding>(FragmentEditBookReviewBinding::inflate) {
    private val args: BookReviewEditFragmentArgs by navArgs()
    private val viewModel: BookViewModel by activityViewModels<BookViewModel> {
        appViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            binding.toolbar.clicks().onEach {
                findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            etComment.setText(args.content)
            reviewRating.rating = args.score
            tvEdit.clicks().throttleFirst(throttleTime).onEach {
                val newContent = etComment.text.toString()
                val score = reviewRating.rating
                if (newContent.isNotEmpty()) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.updateBookReview(
                            args.bookId,
                            args.reviewId,
                            newContent,
                            score
                        )
                        viewModel.updateReviewState.collectLatest { state ->
                        when(state) {
                            is UiState.Loading -> progress.progressCircular.isVisible = true
                            is UiState.Success -> {
                                progress.progressCircular.isVisible = false
                                findNavController().navigateUp()
                                CustomSnackBar.snackBar(root, requireContext(), "서평이 수정되었습니다")
                            }
                            is UiState.Error -> {
                                progress.progressCircular.isVisible = false
                                findNavController().navigateUp()
                                CustomSnackBar.snackBar(root, requireContext(), "다시 시도해주세요")
                            }
                        }
                        }
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }
}