package com.jeong.sesac.sai.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentCompletedFindsBinding
import com.jeong.sesac.sai.util.BaseFragment

/** writer: 정지영
 *
 * Completed finds fragment
 * 찾기 완료한 쪽지
 *
 * 레코드(기록) -> 찾기 완료한 쪽지
 */
class CompletedFindsFragment :
    BaseFragment<FragmentCompletedFindsBinding>(FragmentCompletedFindsBinding::inflate) {

    private val args: CompletedFindsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        with(binding) {
            selectedImage.setImageResource(args.imageResId)
            selectedTitle.text = args.title
            selectedDescription.text = args.description
        }
    }
}