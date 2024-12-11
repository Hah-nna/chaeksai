package com.jeong.sesac.sai.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

    private val args: BookmarkedNotesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        with(binding) {
            // Set selected image and texts
            selectedImage.setImageResource(args.imageResId)
            selectedTitle.text = args.title
            selectedDescription.text = args.description
        }
    }
}