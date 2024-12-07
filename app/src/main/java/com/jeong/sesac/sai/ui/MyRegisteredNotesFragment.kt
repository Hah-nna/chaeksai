package com.jeong.sesac.sai.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentMyRegisteredNotesBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.viewmodel.MyRegisteredNotesViewModel

/** writer: 정지영
 *
 * My registered notes fragment
 * 내가 등록한 쪽지 프레그먼트
 *
 * 레코드(기록) -> 내가 등록한 쪽지
 */
class MyRegisteredNotesFragment :
    BaseFragment<FragmentMyRegisteredNotesBinding>(FragmentMyRegisteredNotesBinding::inflate) {

    companion object {
        fun getInstance() = MyRegisteredNotesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentMyRegisteredNotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}