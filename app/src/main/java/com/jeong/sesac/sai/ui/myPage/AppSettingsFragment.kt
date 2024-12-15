package com.jeong.sesac.sai.ui.myPage

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentAppSettingsBinding
import com.jeong.sesac.sai.util.APP_SETTING_TOOLBAR_TITLE
import com.jeong.sesac.sai.util.BaseFragment

/** writer: 정지영
 *
 * App settings fragment
 * 앱 설정 프래그먼트
 *
 * 마이페이지 -> 설정 -> 앱 설정
 */
class AppSettingsFragment :
    BaseFragment<FragmentAppSettingsBinding>(FragmentAppSettingsBinding::inflate) {

    companion object {
        fun getInstance() = AppSettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentAppSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.toolbar.toolbarView) {
            title = APP_SETTING_TOOLBAR_TITLE
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        // 캐시 데이터 삭제 버튼 클릭 이벤트

        binding.deleteCashData.setOnClickListener {
            showDeleteCashDataDialog()
        }
        // 버튼 상태 토글 설정
        setupButtonToggle(binding.button1)
        setupButtonToggle(binding.button2)
        setupButtonToggle(binding.button3)
        setupButtonToggle(binding.button4)
        setupButtonToggle(binding.button5)
    }

    private fun setupButtonToggle(button: androidx.appcompat.widget.AppCompatImageButton) {
        button.setOnClickListener {
            // 현재 이미지 확인 후 상태 토글
            val currentTag = button.tag
            if (currentTag == "deactivate") {
                button.setImageResource(R.drawable.activate)
                button.tag = "activate"
            } else {
                button.setImageResource(R.drawable.deactivate)
                button.tag = "deactivate"
            }
        }
    }

    private fun showDeleteCashDataDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("캐시 데이터 삭제")
            .setMessage("캐시 데이터를 삭제하시겠습니까?")
            .setPositiveButton("예") { _, _ ->
                Toast.makeText(requireContext(), "캐시 데이터가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                // 여기에 캐시 데이터 삭제 로직 추가 가능
            }
            .setNegativeButton("아니오", null)
            .show()
    }
}