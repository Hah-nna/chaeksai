package com.jeong.sesac.sai.ui.libraryMap.bookReview

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import coil3.size.Scale
import com.jeong.sesac.feature.model.BookReviewWithUser
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentLibraryBookDetailBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.recycler.bookReview.BookReviewAdapter
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.CommentModalBottomSheet
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

class BookDetailFragment :
    BaseFragment<FragmentLibraryBookDetailBinding>(FragmentLibraryBookDetailBinding::inflate) {
    private val args: BookDetailFragmentArgs by navArgs()
    private lateinit var preference: AppPreferenceManager
    private lateinit var bookReviewAdapter: BookReviewAdapter
    private val viewModel: BookViewModel by activityViewModels<BookViewModel> {
        appViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = AppPreferenceManager.getInstance(requireContext())
        Log.d("preference init", "${::preference.isInitialized}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appbar.clicks().onEach {
            findNavController().navigateUp()
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        bookReviewAdapter = BookReviewAdapter(
            preference.userId,
            viewLifecycleOwner.lifecycleScope
        ) { bookReview, isMyReview ->
            showBottomSheet(bookReview, isMyReview)
        }
        Log.d("init reviewddapter", "${::bookReviewAdapter.isInitialized}")

        binding.rvComments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bookReviewAdapter
        }

        getBookInfo()
        getBookReviews()
        createBookReview()
    }

    private fun showBottomSheet(bookReview: BookReviewWithUser, isMyReview: Boolean) {
        CommentModalBottomSheet(
            context = viewLifecycleOwner.lifecycleScope,
            isCommentUser = isMyReview,
            onEditClick = {
                val action = BookDetailFragmentDirections.actionFragmentLibraryBookDetailToFragmentEditLibraryBookReview(
                    args.bookId, bookReview.id, bookReview.content, bookReview.score
                )
                findNavController().navigate(action)
            },
            onDeleteClick = {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.deleteBookReview(args.bookId, bookReview.id)
                    viewModel.deleteReviewState.collectLatest { state ->
                        when (state) {
                            is UiState.Loading -> binding.progress.progressCircular.isVisible = true
                            is UiState.Success -> {
                                binding.progress.progressCircular.isVisible = false
                                CustomSnackBar.snackBar(binding.root, requireContext(), "리뷰를 성공적으로 삭제했습니다")
                            }

                            is UiState.Error -> {
                                binding.progress.progressCircular.isVisible = false
                                CustomSnackBar.snackBar(binding.root, requireContext(), "에러가 발생했습니다\n다시 시도해주세요")
                            }
                        }
                    }
                }

            },
            onReportClick = {
                // commentViewModel.reportComment
            }
        ).show(childFragmentManager, CommentModalBottomSheet.TAG)
    }

    private fun getBookReviews() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getBookReviews(args.bookId)
            viewModel.getReviewsState.collectLatest { state ->
                when(state) {
                    is UiState.Loading -> {
                        binding.progress.progressCircular.isVisible = true
                        binding.tvNoBookReview.isVisible = false
                    }
                    is UiState.Success -> {
                        binding.progress.progressCircular.isVisible = false
                        binding.tvNoBookReview.isVisible = true
                        binding.tvBookReviewsCount.text = "서평 총 ${state.data.size}개"
                        bookReviewAdapter.submitList(state.data)
                    }
                    is UiState.Error -> {
                        binding.progress.progressCircular.isVisible = false
                        binding.tvNoBookReview.isVisible = true
                        CustomSnackBar.snackBar(binding.root, requireContext(), state.error)
                    }
                }

            }
        }
    }

    private fun getBookInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getBook(args.libraryName, args.bookId)
                viewModel.bookState.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> binding.progress.progressCircular.isVisible = true
                        is UiState.Success -> {
                            binding.progress.progressCircular.isVisible = false
                            with(binding) {
                                Log.d("book info", "${state.data}")
                                tvBookTitle.text = state.data.title
                                tvPublishYear.text = state.data.publish_date.substring(0..3)
                                tvPublisher.text = state.data.publisher
                                tvPage.text = state.data.page
                                tvForm.text = state.data.form

                                ivBookImg.load(state.data.book_img) {
                                    crossfade(true)
                                    scale(Scale.FIT)
                                    fallback(R.drawable.no_img)
                                    error(R.drawable.no_img)
                                }
                                Log.d("책소개 데이터", state.data.introduction)

                                tvContent.text = if (state.data.introduction.isNotEmpty()) {
                                    state.data.introduction
                                } else "해당 책은 내용이 제공되는 내용이 없습니다"

                            }
                        }

                        is UiState.Error -> {
                            binding.progress.progressCircular.isVisible = false
                            Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                            throw Exception("책 정보를 가져오지 못했습니다")
                        }


                    }

                }

            }
        }
    }

    private fun createBookReview() {
        binding.btnRegisterComment.clicks().throttleFirst(throttleTime).onEach {
            val content = binding.tvComment.text.toString()
            val score = binding.reviewRating.rating

           viewModel.createBookReview(preference.userId,
               content, args.bookId, score, args.libraryName )
                viewModel.createReviewState.collectLatest { state ->
                    when(state) {
                        is UiState.Loading -> binding.progress.progressCircular.isVisible = true
                        is UiState.Success -> {
                            binding.progress.progressCircular.isVisible = false
                            CustomSnackBar.snackBar(binding.root, requireContext(), "성공")
                            binding.tvComment.text?.clear()
                        }
                        is UiState.Error -> {
                            binding.progress.progressCircular.isVisible = false
                            CustomSnackBar.snackBar(binding.root, requireContext(), state.error)
                        }
                    }
                }

        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}