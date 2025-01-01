package com.jeong.sesac.sai.ui.searchRegister.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.FragmentRegisterNoteBinding
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class RegisterNoteFragment : BaseFragment<FragmentRegisterNoteBinding>(FragmentRegisterNoteBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val content = "이 책을 읽는 모두 다 행복해지기를 바랍니다 ㅎㅎ"
        with(binding) {
            toolbar.toolbarView.setTitle(R.string.BACK_TOOLBAR_TITLE)
            toolbar.toolbarView.clicks().throttleFirst(throttleTime).onEach {
                findNavController().navigateUp()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            /**
             * checkedChanges() : Unit을 반환하는 flow
             * flowWithLifeCycle(lifecycle, lifecycleState) : repeatOnLifeCycle이랑 비슷
             * 라이프사이클 상태에 따라서 블록을 실행
             * launchIn() : checkedChanges()에서 emit한 값을 collect해서 코루틴에서 실행시키는 놈
             * 실제로 여기서 flow에서 emit한 값을 수집하는 코루틴을 만들고 실행
             * */
            btnRegisterHint.clicks().throttleFirst(throttleTime).onEach {
                Log.d("btnRegisterHint", "click!!!!")
                val action = RegisterNoteFragmentDirections.actionFragmentRegisterNoteToFragmentRegisterDetail("청량리도서관", content)
                findNavController().navigate(action)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }
}