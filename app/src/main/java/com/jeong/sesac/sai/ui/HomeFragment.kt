package com.jeong.sesac.sai.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.sesac.sai.databinding.FragmentHomeBinding
import com.jeong.sesac.sai.recycler.gridRecycler.GridNotesAdapter
import com.jeong.sesac.sai.recycler.horizontalRecycler.HorizontalNotesAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.WeeklyNoteMockData
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var weeklyNoteAdapter: HorizontalNotesAdapter
    private lateinit var recentlyFoundAdapter : GridNotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weeklyNoteAdapter = HorizontalNotesAdapter { weeklyNote ->
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentWeeklyNoteDetail(weeklyNote)
            findNavController().navigate(action)
        }

        recentlyFoundAdapter = GridNotesAdapter { foundNote ->
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentRecentlyFoundNotesDetail(foundNote)

            findNavController().navigate(action)

        }


        with(binding) {
            rvWeeklyNotes.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = this@HomeFragment.weeklyNoteAdapter
            }

            rvRecentlyFoundNotes.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = this@HomeFragment.recentlyFoundAdapter
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
            tvWeeklyNotesMore.clicks().onEach {
                val action = HomeFragmentDirections.actionFragmentHomeToFragmentWeeklyNotes(WeeklyNoteMockData.notesList.first()
                )
                findNavController().navigate(action)
                Log.d("HOME", "실행")
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            tvRecentlyFoundNotesMore.clicks().onEach {
                Log.d("HOME", "실행")
                val action = HomeFragmentDirections.actionFragmentHomeToFragmentRecentlyFoundNotes()
                findNavController().navigate(action)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        }
        weeklyNoteAdapter.submitList(WeeklyNoteMockData.notesList)
        recentlyFoundAdapter.submitList(WeeklyNoteMockData.notesList)
    }

}