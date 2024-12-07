package com.jeong.sesac.sai.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentBookmarkedNotesBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.viewmodel.BookmarkedNotesViewModel

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