package com.jeong.sesac.sai.ui.myPage

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentAppSettingsBinding
import com.jeong.sesac.sai.util.APP_SETTING_TOOLBAR_TITLE
import com.jeong.sesac.sai.util.BUTTON_TAG_ACTIVATE
import com.jeong.sesac.sai.util.BUTTON_TAG_DEACTIVATE
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.DIALOG_BUTTON_TEXT_NO
import com.jeong.sesac.sai.util.DIALOG_BUTTON_TEXT_YES
import com.jeong.sesac.sai.util.DIALOG_MESSAGE_DELETE_CASH
import com.jeong.sesac.sai.util.DIALOG_TITLE_DELETE_CASH
import com.jeong.sesac.sai.util.TOAST_MESSAGE_CASH_DELETED
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.activity.backPresses
import ru.ldralighieri.corbind.appcompat.navigationClicks
import ru.ldralighieri.corbind.view.clicks

/**
 * writer: 정지영
 *
 * AppSettingsFragment 클래스
 * 마이페이지 -> 설정 -> 앱 설정 화면을 담당하는 프래그먼트
 */
class AppSettingsFragment :
    BaseFragment<FragmentAppSettingsBinding>(FragmentAppSettingsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            // 툴바 설정 및 뒤로가기 이벤트 처리
            toolbar.toolbarView.apply {
                title = APP_SETTING_TOOLBAR_TITLE
                navigationClicks()
                    .onEach { findNavController().navigateUp() }
                    .launchIn(lifecycleScope)
            }

            // 뒤로가기 버튼 동작
            requireActivity().onBackPressedDispatcher.backPresses(viewLifecycleOwner)
                .onEach { findNavController().navigateUp() }
                .launchIn(lifecycleScope)

            // 캐시 데이터 삭제 버튼 클릭 이벤트
            deleteCashData.clicks()
                .onEach { showDeleteCashDataDialog() }
                .launchIn(lifecycleScope)

            // 버튼 상태 토글 이벤트 설정
            listOf(button1, button2, button3, button4, button5).forEach { button ->
                button.clicks()
                    .onEach { toggleButtonState(button) }
                    .launchIn(lifecycleScope)
            }
        }
    }

    // 버튼 상태 토글 로직
    private fun toggleButtonState(button: androidx.appcompat.widget.AppCompatImageButton) {
        val currentTag = button.tag
        if (currentTag == BUTTON_TAG_DEACTIVATE) {
            button.setImageResource(R.drawable.activate)
            button.tag = BUTTON_TAG_ACTIVATE
        } else {
            button.setImageResource(R.drawable.deactivate)
            button.tag = BUTTON_TAG_DEACTIVATE
        }
    }

    // 캐시 데이터 삭제 다이얼로그 표시
    private fun showDeleteCashDataDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(DIALOG_TITLE_DELETE_CASH)
            .setMessage(DIALOG_MESSAGE_DELETE_CASH)
            .setPositiveButton(DIALOG_BUTTON_TEXT_YES) { _, _ ->
                Toast.makeText(
                    requireContext(),
                    TOAST_MESSAGE_CASH_DELETED,
                    Toast.LENGTH_SHORT
                ).show()
                // 캐시 데이터 삭제 로직 추가 가능
            }
            .setNegativeButton(DIALOG_BUTTON_TEXT_NO, null)
            .show()
    }
}