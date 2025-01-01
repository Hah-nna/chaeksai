package com.jeong.sesac.sai.ui.record

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.ItemTabRecyclerBinding
import com.jeong.sesac.sai.recycler.GridDecoration
import com.jeong.sesac.sai.recycler.Record.RecordRecyclerAdapter
import com.jeong.sesac.sai.ui.RecordFragmentDirections
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.WeeklyNoteMockData

class RecordListFragment : BaseFragment<ItemTabRecyclerBinding>(ItemTabRecyclerBinding::inflate) {


    private lateinit var recordAdapter : RecordRecyclerAdapter

    companion object {
        fun getInstance(position : Int) =
            RecordListFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    /**
     * recordAdapter의 {...} 은 callBack을 구현한 것임
     * RecordRecyclerAdapter에서 아이템들이 클릭될 때 arguement에 있는 position이라는 키를 가진 value의 값을 보고
     * 각 아이템을 누르면 어느 프래그먼트로 이동해야하는지를 설정함
     * */
        recordAdapter = RecordRecyclerAdapter { weeklyNotesInfo ->
        val action = when (arguments?.getInt("position") ?: 0) {
            0 -> {
                RecordFragmentDirections.actionFragmentRecordToFragmentMyRegisteredNotes(
                    "등록한쪽지", "이건쪽지", R.drawable.example_note
                )
            }

            1 -> {
                RecordFragmentDirections.actionFragmentRecordToFragmentCompletedFinds(
                    "찾은쪽지", "이건쪽지", R.drawable.note03
                )
            }

            else -> {
                RecordFragmentDirections.actionFragmentRecordToFragmentBookmarkedNotes(
                    "북마크쪽지", "이건쪽지", R.drawable.note02
                )
            }

        }
        findNavController().navigate(action)
    }

        with(binding.rvContainer) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridDecoration(requireContext()))
            adapter = recordAdapter
        }
        recordAdapter.submitList(WeeklyNoteMockData.notesList)
    }
}