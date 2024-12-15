package com.jeong.sesac.sai.ui.record

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentMyRegisteredNotesBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.MY_REGISTERED_NOTES_TOOLBAR_TITLE
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.activity.backPresses
import ru.ldralighieri.corbind.view.clicks

class MyRegisteredNotesFragment :
    BaseFragment<FragmentMyRegisteredNotesBinding>(FragmentMyRegisteredNotesBinding::inflate) {

    private val args: MyRegisteredNotesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // Toolbar 설정
            toolbar.toolbarView.apply {
                title = MY_REGISTERED_NOTES_TOOLBAR_TITLE

                // Corbind로 클릭 이벤트 처리
                lifecycleScope.launch {
                    clicks().collect { findNavController().navigateUp() }
                }
            }

            // 뒤로가기 버튼 처리
            requireActivity().onBackPressedDispatcher.backPresses(viewLifecycleOwner)
                .onEach { findNavController().navigateUp() }
                .launchIn(lifecycleScope)


            // 데이터 바인딩
            selectedImage.setImageResource(args.imageResId)
            selectedTitle.text = args.title
            selectedDescription.text = args.description
        }
    }
}