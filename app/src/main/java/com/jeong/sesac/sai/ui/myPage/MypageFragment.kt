package com.jeong.sesac.sai.ui.myPage

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.databinding.FragmentMypageBinding
import com.jeong.sesac.sai.util.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class MypageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnArrow.clicks().onEach {
            val action = MypageFragmentDirections.actionFragmentMyPageToFragmentMyProfile()
            findNavController().navigate(action)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }





}
