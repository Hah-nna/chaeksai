package com.jeong.sesac.sai.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.data.RecordItem
import com.jeong.sesac.sai.databinding.FragmentGridRecyclerViewBinding
import com.jeong.sesac.sai.ui.RecordAdapter

class GridRecyclerViewFragment : Fragment() {

    private var _binding: FragmentGridRecyclerViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGridRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_POSITION) ?: 0
        val items = getItemsForTab(position)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = RecordAdapter(items) { item ->
            // 아이템 클릭 시 네비게이션 동작과 데이터 전달
            findNavController().navigate(
                item.destinationId,
                bundleOf(
                    "title" to item.title,
                    "description" to item.description,
                    "imageResId" to item.imageResId
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getItemsForTab(position: Int): List<RecordItem> {
        return when (position) {
            0 -> listOf(
                RecordItem("등록한 쪽지", "등록한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_my_registered_notes),
                RecordItem("등록한 쪽지", "등록한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_my_registered_notes),
                RecordItem("등록한 쪽지", "등록한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_my_registered_notes),
                RecordItem("등록한 쪽지", "등록한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_my_registered_notes),
                RecordItem("등록한 쪽지", "등록한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_my_registered_notes),
                RecordItem("등록한 쪽지", "등록한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_my_registered_notes),
                RecordItem("등록한 쪽지", "등록한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_my_registered_notes),
                RecordItem("등록한 쪽지", "등록한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_my_registered_notes))
            1 -> listOf(
                RecordItem("찾은 쪽지", "찾은 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_completed_finds),
                RecordItem("찾은 쪽지", "찾은 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_completed_finds),
                RecordItem("찾은 쪽지", "찾은 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_completed_finds),
                RecordItem("찾은 쪽지", "찾은 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_completed_finds),
                RecordItem("찾은 쪽지", "찾은 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_completed_finds),
                RecordItem("찾은 쪽지", "찾은 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_completed_finds),
                RecordItem("찾은 쪽지", "찾은 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_completed_finds),
                RecordItem("찾은 쪽지", "찾은 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_completed_finds)
            )
            2 -> listOf(
                RecordItem("찜한 쪽지", "찜한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_bookmarkedNotes),
                RecordItem("찜한 쪽지", "찜한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_bookmarkedNotes),
                RecordItem("찜한 쪽지", "찜한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_bookmarkedNotes),
                RecordItem("찜한 쪽지", "찜한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_bookmarkedNotes),
                RecordItem("찜한 쪽지", "찜한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_bookmarkedNotes),
                RecordItem("찜한 쪽지", "찜한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_bookmarkedNotes),
                RecordItem("찜한 쪽지", "찜한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_bookmarkedNotes),
                RecordItem("찜한 쪽지", "찜한 쪽지 상세 설명", R.drawable.example_note, R.id.fragment_bookmarkedNotes))
            else -> emptyList()
        }
    }

    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): GridRecyclerViewFragment {
            return GridRecyclerViewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                }
            }
        }
    }
}
