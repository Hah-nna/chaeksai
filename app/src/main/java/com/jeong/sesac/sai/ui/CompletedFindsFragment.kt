package com.jeong.sesac.sai.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentCompletedFindsBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.viewmodel.CompletedFindsViewModel

/** writer: 정지영
 *
 * Completed finds fragment
 * 찾기 완료한 쪽지
 *
 * 레코드(기록) -> 찾기 완료한 쪽지
 */
class CompletedFindsFragment :
    BaseFragment<FragmentCompletedFindsBinding>(FragmentCompletedFindsBinding::inflate) {

    companion object {
        fun getInstance() = CompletedFindsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentCompletedFindsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}