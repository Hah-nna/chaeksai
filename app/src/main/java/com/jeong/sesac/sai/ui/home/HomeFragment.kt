package com.jeong.sesac.sai.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.domain.model.NoteFilterType
import com.jeong.sesac.domain.model.SortOrder
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentHomeBinding
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.recycler.newNote.NewNoteAdapter
import com.jeong.sesac.sai.recycler.HorizontalDecoration
import com.jeong.sesac.sai.recycler.weeklyNote.WeeklyNoteAdapter
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.NoteListViewModel
import com.jeong.sesac.sai.viewmodel.WriteNoteViewModel
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var weeklyNoteAdapter: WeeklyNoteAdapter
    private lateinit var recentlyNewAdapter : NewNoteAdapter
    private lateinit var preferenceManger: AppPreferenceManager
    private val viewModel: NoteListViewModel by viewModels<NoteListViewModel> {
        appViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManger = AppPreferenceManager.getInstance(requireContext())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weeklyNoteAdapter = WeeklyNoteAdapter { weeklyNote ->
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentWeeklyNoteDetail(weeklyNote.id)
            findNavController().navigate(action)
        }

        recentlyNewAdapter = NewNoteAdapter { recentlyNewNote ->
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentLibraryNoteDetail(recentlyNewNote.id)
            findNavController().navigate(action)
        }


        with(binding) {
            rvWeeklyNotes.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = this@HomeFragment.weeklyNoteAdapter
                addItemDecoration(HorizontalDecoration(requireContext()))
            }

            rvRecentlyFoundNotes.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = this@HomeFragment.recentlyNewAdapter
                addItemDecoration(HorizontalDecoration(requireContext()))
            }

            /**
             * clicks() : Unit을 emit하는 flow
             * 클릭되었으면 unit을 emit(방출) -> 클릭 되었음을 알 수 있음
             *
             * onEach로 clicks()라는 flow에서 emit한 unit에 해줄 행동을 블록안에 정의함
             * 여기서는 네비게이션 이동
             *
             * launchIn()은 collect와 launch를 합친 것
             * clicks()는 flow고, 터미널 연산자가 있어야 실행됨(cold stream)
             * 그래서 clicks()에서 방출한 값을 launchIn()을 통해서 실행시켜줌
             * 이때 인자로 scope가 들어감
             * viewLifecycleOwner.lifecycleScope로 한 이유는
             * 프래그먼트 뷰의 라이프 사이클에 맞춰 코루틴 스코프(ui 스레드)가 연동되어 뷰의 라이프 사이클에 맞게 코루틴을 실행하고 관리하기 위해서
             * */
            tvWeeklyNotesMore.clicks().throttleFirst(throttleTime).onEach {
                val action = HomeFragmentDirections.actionFragmentHomeToFragmentWeeklyNotes()
                findNavController().navigate(action)
                Log.d("HOME", "실행")
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            tvRecentlyFoundNotesMore.clicks().throttleFirst(throttleTime).onEach {
                Log.d("HOME", "실행")
                val action = HomeFragmentDirections.actionFragmentHomeToFragmentNewNotes()
                findNavController().navigate(action)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }

            // 닉네임 set하는 부분
        try {
            val nickname = preferenceManger.nickName
            binding.tvTitle.text = when(nickname.isEmpty()) {
                true -> getString(R.string.login_required)
                else -> getString(R.string.welcome_message, nickname)
            }

        } catch (e : Exception) {
            throw Error(e.message)
        }

        viewModel.getNoteList(NoteFilterType.ThisWeek(SortOrder.LATEST))
        viewModel.getNoteList(NoteFilterType.ByLikes(false))

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.noteListState.collectLatest { state ->
                    when(state) {
                        is UiState.Loading -> binding.progressCircle.progressCircular.isVisible = true
                        is UiState.Success -> {
                            binding.progressCircle.progressCircular.isVisible = false
                            weeklyNoteAdapter.submitList(state.data)
                        }
                        is UiState.Error -> {
                            binding.progressCircle.progressCircular.isVisible = false
                            Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT)
                        }
                    }

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.noteListState.collectLatest { state ->
                    when(state) {
                        is UiState.Loading -> binding.progressCircle.progressCircular.isVisible = true
                        is UiState.Success -> {
                            binding.progressCircle.progressCircular.isVisible = false
                            recentlyNewAdapter.submitList(state.data)
                        }
                        is UiState.Error -> {
                            binding.progressCircle.progressCircular.isVisible = false
                            Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT)
                        }
                    }

                }
            }
        }
    }
}