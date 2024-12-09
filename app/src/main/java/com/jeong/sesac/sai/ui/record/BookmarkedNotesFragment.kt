package com.jeong.sesac.sai.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeong.sesac.sai.databinding.FragmentBookmarkedNotesBinding
import com.jeong.sesac.sai.util.BaseFragment

/** writer: 정지영
 *
 * Bookmarked notes fragment
 * 내가 찜한 쪽지
 *
 * 레코드(기록) -> 내가 찜한 쪽지
 */
class BookmarkedNotesFragment :
    BaseFragment<FragmentBookmarkedNotesBinding>(FragmentBookmarkedNotesBinding::inflate) {

    companion object {
        fun getInstance() = BookmarkedNotesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentBookmarkedNotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}