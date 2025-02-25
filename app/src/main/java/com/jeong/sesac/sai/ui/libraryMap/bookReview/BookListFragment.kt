package com.jeong.sesac.sai.ui.libraryMap.bookReview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.sai.databinding.FragmentLibraryBookListBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.recycler.GridDecoration
import com.jeong.sesac.sai.recycler.book.BookAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.BookViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class BookListFragment :
    BaseFragment<FragmentLibraryBookListBinding>(FragmentLibraryBookListBinding::inflate) {
    private val args: BookListFragmentArgs by navArgs()
    private lateinit var bookAdapter: BookAdapter
    private val viewModel: BookViewModel by activityViewModels<BookViewModel> {
        appViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookAdapter = BookAdapter(viewLifecycleOwner.lifecycleScope) { bookInfo ->
            Log.d("cliked book", "${bookInfo.title} ${bookInfo.id}")
            val action =
                BookListFragmentDirections.actionFragmentLibraryMapFragmentToFragmentLibraryBookDetail(
                    args.libraryName, bookInfo.id
                )
            findNavController().navigate(action)
        }

        binding.rvContainer.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@BookListFragment.bookAdapter
            addItemDecoration(GridDecoration(requireContext()))
        }

        with(binding) {
            btnTitle.text = args.libraryName
            fabRegisterBook.clicks().throttleFirst(throttleTime).onEach {
                val action =
                    BookListFragmentDirections.actionFragmentLibraryMapFragmentToFragmentRegisterLibraryBook(
                        args.libraryName
                    )
                findNavController().navigate(action)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getBookList(args.libraryName)
                viewModel.bookListState.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> binding.progress.progressCircular.isVisible = true
                        is UiState.Success -> {
                            binding.progress.progressCircular.isVisible = false
                            binding.tvNoBooks.isVisible = state.data.isEmpty()
                            bookAdapter.submitList(state.data)
                        }
                        is UiState.Error -> {
                            binding.progress.progressCircular.isVisible = false
                            binding.tvNoBooks.isVisible = true
                        }
                    }
                }

        }

        // 뒤로 가기
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

    }
}