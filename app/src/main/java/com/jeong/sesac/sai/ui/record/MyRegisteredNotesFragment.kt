package com.jeong.sesac.sai.ui.record

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentMyRegisteredNotesBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.MY_REGISTERED_NOTES_TOOLBAR_TITLE

class MyRegisteredNotesFragment :
    BaseFragment<FragmentMyRegisteredNotesBinding>(FragmentMyRegisteredNotesBinding::inflate) {

    private val args: MyRegisteredNotesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.toolbar.toolbarView) {
            title = MY_REGISTERED_NOTES_TOOLBAR_TITLE
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }

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