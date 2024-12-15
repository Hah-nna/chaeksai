package com.jeong.sesac.sai.ui.record

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.data.RecordItem
import com.jeong.sesac.sai.databinding.FragmentGridRecyclerViewBinding
import com.jeong.sesac.sai.ui.RecordAdapter
import com.jeong.sesac.sai.util.*

// GridRecyclerViewFragment는 RecyclerView를 Grid 형식으로 보여주는 프래그먼트입니다.
class GridRecyclerViewFragment : Fragment() {

    // 뷰 바인딩 변수를 선언합니다. 뷰가 메모리에서 제거되면 null로 설정됩니다.
    private var _binding: FragmentGridRecyclerViewBinding? = null

    // 안전한 호출을 보장하기 위해 바인딩을 통해 접근합니다.
    private val binding get() = _binding!!

    // 프래그먼트의 뷰를 생성하는 onCreateView 메서드입니다.
    override fun onCreateView(
        inflater: android.view.LayoutInflater, // 레이아웃 인플레이터 객체
        container: android.view.ViewGroup?, // 부모 컨테이너
        savedInstanceState: Bundle? // 프래그먼트 상태 저장 번들
    ): View {

        // FragmentGridRecyclerViewBinding을 인플레이트하고 root 뷰를 반환합니다.
        _binding =
            FragmentGridRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    // 뷰가 생성되고 나서 추가적으로 초기화 작업을 수행하는 onViewCreated 메서드입니다.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 프래그먼트에 전달된 인자에서 탭의 포지션(position)을 가져옵니다.
        val position = arguments?.getInt(ARG_POSITION) ?: 0

        // 포지션에 따라 보여줄 아이템 리스트를 생성합니다.
        val items = getItemsForTab(position)

        // RecyclerView와 어댑터를 설정합니다.
        with(binding) {

            // RecyclerView를 2열의 Grid 레이아웃으로 설정합니다.
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

            // RecordAdapter를 설정하며 클릭 이벤트 리스너를 전달합니다.
            recyclerView.adapter = RecordAdapter(items) { item ->

                // 클릭된 아이템의 destinationId를 사용해 네비게이션 이동을 수행합니다.
                findNavController().navigate(
                    item.destinationId,
                    bundleOf(
                        "title" to item.title, // 아이템의 제목
                        "description" to item.description, // 아이템의 설명
                        "imageResId" to item.imageResId // 아이템의 이미지 리소스 ID
                    )
                )
            }
        }
    }

    // 프래그먼트의 뷰가 파괴될 때 호출되는 onDestroyView 메서드입니다.
    override fun onDestroyView() {
        super.onDestroyView()

        // 메모리 누수를 방지하기 위해 바인딩을 null로 설정합니다.
        _binding = null
    }

    // 주어진 포지션에 따라 각 탭에서 보여줄 아이템 리스트를 반환합니다.
    private fun getItemsForTab(position: Int): List<RecordItem> {

        // 포지션에 따른 destinationId를 설정합니다.
        val destinationId = when (position) {
            0 -> R.id.fragment_my_registered_notes // 첫 번째 탭
            1 -> R.id.fragment_completed_finds // 두 번째 탭
            2 -> R.id.fragment_bookmarkedNotes // 세 번째 탭
            else -> R.id.fragment_my_registered_notes // 기본 값
        }

        // 예제 아이템을 생성합니다.
        val exampleItem = RecordItem(
            title = EXAMPLE_TITLE, // 아이템 제목
            description = EXAMPLE_DESCRIPTION, // 아이템 설명
            imageResId = R.drawable.example_note, // 아이템 이미지 리소스
            destinationId = destinationId // 네비게이션 목적지 ID
        )

        // 예제 아이템 8개로 구성된 리스트를 반환합니다.
        return List(8) { exampleItem }
    }

    companion object {

        // GridRecyclerViewFragment의 인스턴스를 생성하는 팩토리 메서드입니다.
        fun newInstance(position: Int): GridRecyclerViewFragment {
            return GridRecyclerViewFragment().apply {

                // 전달된 포지션 값을 arguments에 저장합니다.
                arguments = bundleOf(ARG_POSITION to position)
            }
        }
    }
}