package com.jeong.sesac.sai.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentWeeklynotesBinding
import com.jeong.sesac.sai.recycler.gridRecycler.GridRecyclerDecoration
import com.jeong.sesac.sai.recycler.horizontalRecycler.HorizontalNotesAdapter
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.WeeklyNoteMockData
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.appcompat.navigationClicks

class WeeklyNotesFragment :
    BaseFragment<FragmentWeeklynotesBinding>(FragmentWeeklynotesBinding::inflate) {

    private lateinit var weeklyNoteAdapter: HorizontalNotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeeklynotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.toolbar.toolbarView) {
            setTitle(R.string.WEEKLY_NOTES_TOOLBAR_TITLE)

            /**
             * https://github.com/LDRAlighieri/Corbind/blob/master/corbind/src/main/kotlin/ru/ldralighieri/corbind/widget/ToolbarNavigationClicks.kt
             * 여기를 참조해서 toolbar 이벤트 설정함
             * navigationClicks()로 만들어진 flow는 Toolbar.setNavigationOnClickListener를 사용한다고해서 사용함
             * */
            navigationClicks().onEach {
                findNavController().navigateUp()
                Log.d("weekly", "실행")
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                findNavController().navigateUp()
            }

            weeklyNoteAdapter = HorizontalNotesAdapter { weeklyDetailNote ->
                val action =
                    WeeklyNotesFragmentDirections.actionFragmentWeeklyNotesToDetail(weeklyDetailNote)
                findNavController().navigate(action)
            }

            with(binding) {
                rvGridNotesList.apply {
                    layoutManager = GridLayoutManager(requireContext(), 2)
                    addItemDecoration(GridRecyclerDecoration(2, 96))
                    adapter = this@WeeklyNotesFragment.weeklyNoteAdapter
                }
            }

            weeklyNoteAdapter.submitList(WeeklyNoteMockData.notesList)
        }
    }
}