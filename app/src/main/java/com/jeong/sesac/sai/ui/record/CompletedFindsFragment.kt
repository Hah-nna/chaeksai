package com.jeong.sesac.sai.ui.record

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentCompletedFindsBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.COMPLETED_FINDS_TOOLBAR_TITLE
import com.jeong.sesac.sai.util.NOTE_ID
import com.kakao.sdk.user.Constants.USER_ID
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.activity.backPresses
import ru.ldralighieri.corbind.view.clicks

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

        with(binding) {

            // Toolbar 설정
            toolbar.toolbarView.apply {
                title = COMPLETED_FINDS_TOOLBAR_TITLE
                lifecycleScope.launch {
                    clicks().collect { findNavController().navigateUp() }
                }
            }

            // 뒤로가기 버튼 처리
            requireActivity().onBackPressedDispatcher.backPresses(viewLifecycleOwner)
                .onEach { findNavController().navigateUp() }
                .launchIn(lifecycleScope)

            // 데이터 설정
            selectedImage.setImageResource(args.imageResId)
            selectedTitle.text = args.title
            selectedDescription.text = args.description

            // 버튼 클릭 이벤트 처리
            lifecycleScope.launch {
                button.clicks().collect {
                    val action = CompletedFindsFragmentDirections
                        .actionFragmentCompletedFindsToFragmentWriteReview(USER_ID, NOTE_ID)
                    findNavController().navigate(action)
                }
            }
        }
    }
}