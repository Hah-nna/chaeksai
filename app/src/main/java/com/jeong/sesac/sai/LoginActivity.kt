package com.jeong.sesac.sai

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.jeong.sesac.sai.databinding.ActivityLoginBinding
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.viewmodel.UserViewModel
import com.jeong.sesac.sai.model.UiState
import com.jeong.sesac.sai.viewmodel.factory.appViewModelFactory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.textChanges

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val preference by lazy { AppPreferenceManager.getInstance(this) }
    private val viewModel: UserViewModel by viewModels<UserViewModel> {
        appViewModelFactory
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        /**
         * editText 포커스 여부에 따라 색 변경
         * */
        setETBackground()

        /**
         * 유효성 검사 및 닉네임 중복검사
         * */
        checkValidNickname()


        /*
        * 닉네임 받아서 유저생성 및 preference에 저장
        * */
        with(binding) {
            btnSetUser.clicks().throttleFirst(throttleTime).onEach {
                val nickname = etvNickname.text.toString()
                setUser(nickname)
            }.launchIn(lifecycleScope)
        }
    }

    /**
     * 닉네임 유효성 검사 및 중복검사 함수
     * 닉네임 유효범위 : 숫자, 한글, 영어 조합으로 총 8글자까지만 가능
     * 닉네임 비유효범위 : 숫자로만 이루어져있을 때, 특수문자, 공백, 2글자 미만
     *
     * */
    @OptIn(FlowPreview::class)
    private fun checkValidNickname() {

        binding.etvNickname.textChanges()
            // 처음 emit되는 빈값 drop
            .dropInitialValue()
            .onEach { char ->
                // 로그를 찍어보니 하나하나 emit되는 char 값이 계속 더해짐 (ex: n -> ni -> nic -> nick 이런식으로)
                val inputText = char.toString()
                // 유효성 검사 함
                val isValidText = isValidNickname(inputText)
                when {
                    inputText.length < 2 -> {
                        binding.tvNicknameCheck.setHint("2글자 이상 8글자 이하만 가능합니다")
                        binding.tvNicknameCheck.setHintTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.inValid
                            )
                        )
                        binding.etvNickname.backgroundTintList =
                            ContextCompat.getColorStateList(applicationContext, R.color.inValid)
                        return@onEach
                    }
                    // 유효성 검사 통과 못했을 시
                    !isValidText -> {
                        binding.tvNicknameCheck.setHint("특수문자와 공백은 사용할 수 없습니다")
                        binding.tvNicknameCheck.setHintTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.red
                            )
                        )
                        binding.etvNickname.backgroundTintList =
                            ContextCompat.getColorStateList(applicationContext, R.color.red)
                        return@onEach
                    }
                    // 입력된 텍스트가 8글자 이상(유효범위 벗어남)
                    inputText.length > 8 -> {
                        binding.tvNicknameCheck.setHint("8글자까지만 가능합니다")
                        binding.tvNicknameCheck.setHintTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.inValid
                            )
                        )
                        binding.etvNickname.backgroundTintList =
                            ContextCompat.getColorStateList(applicationContext, R.color.inValid)
                        return@onEach
                    }
                    // 입력된 텍스트가 2글자 미만(유효범위 벗어남)

                    // 통과한 경우
                    // 메세지는 딱히 없고 색깔만 변경
                    else -> {
                        binding.tvNicknameCheck.setHint("")
                        binding.tvNicknameCheck.setHintTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.valid
                            )
                        )
                        binding.etvNickname.backgroundTintList =
                            ContextCompat.getColorStateList(applicationContext, R.color.valid)
                    }
                }
            }
            // 위의 text가 더 생성이 안 되면(이벤트 x) 마지막 이벤트 0.5후에 아래를 실행
            .debounce(500L)
            .onEach { nickName ->
                val nickname = nickName.toString()
                // 닉네임 전체에 대해서 다시 유효성 검사 시행
                if (isValidNickname(nickname)) {
                    // 리턴값이 참이면 중복검사
                    viewModel.checkDuplicatedNickname(nickname)
                }

            }
            .launchIn(lifecycleScope)


        lifecycleScope.launch {
            viewModel.duplicateState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.progressCircular.isVisible = true
                    }

                    is UiState.Success -> {
                        binding.progressBar.progressCircular.isVisible = false
                        if (state.data) {
                            binding.tvNicknameCheck.setHint("이미 존재하는 닉네임입니다")
                            binding.etvNickname.backgroundTintList =
                                ContextCompat.getColorStateList(applicationContext, R.color.inValid)
                            binding.tvNicknameCheck.setHintTextColor(
                                ContextCompat.getColor(
                                    this@LoginActivity,
                                    R.color.inValid
                                )
                            )
                            binding.btnSetUser.isEnabled = false
                        } else {
                            binding.tvNicknameCheck.setHint("사용할 수 있는 닉네임입니다")
                            binding.etvNickname.backgroundTintList =
                                ContextCompat.getColorStateList(applicationContext, R.color.valid)
                            binding.tvNicknameCheck.setHintTextColor(
                                ContextCompat.getColor(
                                    this@LoginActivity,
                                    R.color.valid
                                )
                            )
                            binding.btnSetUser.isEnabled = true
                        }
                    }

                    is UiState.Error -> {
                        binding.progressBar.progressCircular.isVisible = false
                        binding.tvNicknameCheck.setHint("오류가 발생했습니다 다시 시도해주세요")
                        binding.etvNickname.backgroundTintList =
                            ContextCompat.getColorStateList(applicationContext, R.color.inValid)
                        binding.tvNicknameCheck.setHintTextColor(
                            ContextCompat.getColor(
                                this@LoginActivity,
                                R.color.inValid
                            )
                        )
                        binding.btnSetUser.isEnabled = false
                    }
                }
            }
        }
    }

    private fun isValidNickname(nickname: String): Boolean {
        val onlyNumber = "[0-9]".toRegex()
        val validCharsRegex = "^[a-zA-Z가-힣0-9]+$".toRegex()

        return when {
            nickname.isEmpty() || nickname.length < 2 || nickname.length > 8 -> false
            !validCharsRegex.matches(nickname) -> false
            onlyNumber.matches(nickname) -> false
            else -> true
        }
    }

    // 포커스됨에 따라서 edit text view 색깔 변경
    private fun setETBackground() {
        with(binding.etvNickname) {
            setOnFocusChangeListener { _, hasFocus ->
                backgroundTintList = when (hasFocus) {
                    true ->
                        ContextCompat.getColorStateList(applicationContext, R.color.tertiary)

                    else -> ContextCompat.getColorStateList(applicationContext, R.color.black)
                }
            }
        }
    }

    // 유저 생성
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUser(nickname: String) {
        viewModel.setUserInfo(nickname)
        lifecycleScope.launch {
            viewModel.userCreateState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.progressCircular.isVisible = false
                    }

                    is UiState.Success -> {
                        preference.apply {
                            nickName = nickname
                            userId = state.data
                        }
                        Log.d("userId", state.data)
                        Intent(this@LoginActivity, MainActivity::class.java).run {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            Log.d("intent to mainActivity", "INTENT")
                            startActivity(this)
                        }
                        finish()
                    }
                    is UiState.Error -> {
                        Toast.makeText(this@LoginActivity, "에러 발생", LENGTH_SHORT).show()
                    }
                }
            }
        }

    }


}