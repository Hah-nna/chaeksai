package com.jeong.sesac.sai.ui.record

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.domain.model.NoteFilterType
import com.jeong.sesac.sai.databinding.ItemTabRecyclerBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.recycler.GridDecoration
import com.jeong.sesac.sai.recycler.record.RecordRecyclerAdapter
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.viewmodel.NoteListViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecordListFragment : BaseFragment<ItemTabRecyclerBinding>(ItemTabRecyclerBinding::inflate) {

    private lateinit var recordAdapter: RecordRecyclerAdapter
    private lateinit var preference: AppPreferenceManager
    private val viewModel: NoteListViewModel by viewModels <NoteListViewModel> { appViewModelFactory }

    companion object {
        fun getInstance(position: Int) =
            RecordListFragment().apply {
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
        /**
         * recordAdapter의 {...} 은 callBack을 구현한 것임
         * RecordRecyclerAdapter에서 아이템들이 클릭될 때 arguement에 있는 position이라는 키를 가진 value의 값을 보고
         * 각 아이템을 누르면 어느 프래그먼트로 이동해야하는지를 설정함
         * */
        recordAdapter = RecordRecyclerAdapter { note ->
            val action =
                RecordFragmentDirections.actionFragmentRecordToFragmentLibraryNoteDetail(
                    note.id
                )
            findNavController().navigate(action)
        }

        with(binding.rvContainer) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridDecoration(requireContext()))
            adapter = this@RecordListFragment.recordAdapter
        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val filteredList = when (arguments?.getInt("position") ?: 0) {
                    0 -> NoteFilterType.MyNotes
                    else -> NoteFilterType.MyLikedNotes
                }
                viewModel.getNoteList(filteredList, preference.nickName)

                viewModel.noteListState.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> binding.progress.progressCircular.isVisible = true
                        is UiState.Success -> {
                            binding.progress.progressCircular.isVisible = false
                            recordAdapter.submitList(state.data)
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