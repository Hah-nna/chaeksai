package com.jeong.sesac.sai.ui.found
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentWriteReviewBinding
import com.jeong.sesac.sai.util.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.jeong.sesac.sai.util.DEFAULT_REVIEW_CONTENT
import com.jeong.sesac.sai.util.NOTE_ID
import com.kakao.sdk.user.Constants.USER_ID
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.activity.backPresses
import ru.ldralighieri.corbind.view.clicks

class WriteReviewFragment :
    BaseFragment<FragmentWriteReviewBinding>(FragmentWriteReviewBinding::inflate) {

    private val args: WriteReviewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val content = "아 너무 좋았구요 이 책은 꼭 읽어보세요"
        with(binding) {
            toolbar.toolbarView.setTitle(R.string.BACK_TOOLBAR_TITLE)
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
                binding.btnCompletedReview.clicks().collect {
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
