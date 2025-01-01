package com.jeong.sesac.sai.ui.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.jeong.sesac.datamodule.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.datamodule.repository.LoginRepositoryImpl
import com.jeong.sesac.domain.model.UserInfo
import com.jeong.sesac.sai.MainActivity
import com.jeong.sesac.sai.databinding.FragmentLoginByNicknameBinding
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.BaseFragment
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.AuthViewModel
import com.jeong.sesac.sai.viewmodel.factory.AuthViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import java.time.LocalDateTime

class LoginFragment : BaseFragment<FragmentLoginByNicknameBinding>(FragmentLoginByNicknameBinding::inflate) {
    private val loginRepo = LoginRepositoryImpl(FireBaseDataSourceImpl())
    private val preference by lazy { AppPreferenceManager.getInstance(requireContext())}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LoginFragment onCreateView", "create")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LoginFragment onViewCreated", "create")
        with(binding) {
            btnSetUser.clicks().throttleFirst(throttleTime)
                .onEach {
                    val nickname = etvNickname.text.toString()
                    if (nickname.isNotEmpty()) {
                        try {
                            lifecycleScope.launch {
                                val success = loginRepo.setUser(
                                    UserInfo(
                                        "",
                                        nickname,
                                        "",
                                        LocalDateTime.now().toString()
                                    )
                                )

                                if (success) {
                                    preference.nickName = nickname
                                    startActivity(Intent(requireContext(), MainActivity::class.java))
                                    requireActivity().finish()
                                } else {
                                    // 실패 처리
                                    throw Error("에러입니다아아아아아아아아아아아아아아아아")
                                    Log.e("LoginFragment", "Failed to create user")
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("LoginFragment", "Error: ${e.message}")
                        }
                    }
                }
                .launchIn(lifecycleScope)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("LoginFragment onPause", "onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("LoginFragment onDestroyView", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LoginFragment onDestroy", "onDestroy")
    }
}


