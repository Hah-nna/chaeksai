package com.jeong.sesac.sai.ui.record

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentCompletedFindsBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.NOTE_ID
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.factory.viewModelFactory
import com.jeong.sesac.sai.viewmodel.RecordViewModel
import com.kakao.sdk.user.Constants.USER_ID
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val recordViewModel by activityViewModels<RecordViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            // Toolbar 설정
            toolbar.toolbarView.apply {
                setTitle(R.string.COMPLETED_FINDS_TOOLBAR_TITLE)
                clicks().throttleFirst(throttleTime).onEach { findNavController().navigateUp() }
                    .launchIn(lifecycleScope)
            }

            // 뒤로가기 버튼 처리
            requireActivity().onBackPressedDispatcher.backPresses(viewLifecycleOwner)
                .throttleFirst(throttleTime)
                .onEach { findNavController().navigateUp() }
                .launchIn(lifecycleScope)

            // 데이터 설정
            selectedImage.setImageResource(args.imageResId)
            selectedTitle.text = args.title
            selectedDescription.text = args.description

            // 버튼 클릭 이벤트 처리
                button.clicks()
                    .throttleFirst(throttleTime)
                    .onEach {
                    val action = CompletedFindsFragmentDirections
                        .actionFragmentCompletedFindsToFragmentWriteReview(USER_ID, NOTE_ID)
                    findNavController().navigate(action)
            }.launchIn(lifecycleScope)
        }
    }
}