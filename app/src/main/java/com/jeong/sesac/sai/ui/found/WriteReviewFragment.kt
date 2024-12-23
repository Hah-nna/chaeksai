package com.jeong.sesac.sai.ui.found
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.databinding.FragmentWriteReviewBinding
import com.jeong.sesac.sai.util.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.jeong.sesac.sai.util.DEFAULT_REVIEW_CONTENT
import com.jeong.sesac.sai.util.NOTE_ID
import com.jeong.sesac.sai.util.WRITE_REVIEW_TOOLBAR_TITLE
import com.kakao.sdk.user.Constants.USER_ID
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.activity.backPresses
import ru.ldralighieri.corbind.view.clicks

class WriteReviewFragment :
    BaseFragment<FragmentWriteReviewBinding>(FragmentWriteReviewBinding::inflate) {

    private val args: WriteReviewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.toolbarView.title = BACK_TOOLBAR_TITLE
            btnCompletedReview.clicks().onEach {
                val action = WriteReviewFragmentDirections.actionFragmentWriteReviewToFragmentReviewCompleted("1234", "1", content)
                findNavController().navigate(action)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            toolbar.toolbarView.clicks().onEach {
                findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        }
       
            // 뒤로가기 버튼 처리
            requireActivity().onBackPressedDispatcher.backPresses(viewLifecycleOwner)
                .onEach { findNavController().navigateUp() }
                .launchIn(lifecycleScope)

            // 리뷰 작성 완료 버튼 클릭 이벤트 처리
            lifecycleScope.launch {
                btnCompletedReview.clicks().collect {
                    val action =
                        WriteReviewFragmentDirections
                            .actionFragmentWriteReviewToFragmentReviewCompleted(
                                USER_ID, NOTE_ID, DEFAULT_REVIEW_CONTENT
                            )
                    findNavController().navigate(action)
                }
            }
        }
    }
}