package com.jeong.sesac.sai

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.jeong.sesac.datamodule.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.datamodule.repository.LoginRepositoryImpl
import com.jeong.sesac.domain.model.UserInfo
import com.jeong.sesac.sai.databinding.ActivityLoginBinding
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.textChanges
import java.time.LocalDateTime

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginRepo = LoginRepositoryImpl(FireBaseDataSourceImpl())
    private val preference by lazy { AppPreferenceManager.getInstance(this) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("LoginActivity create", "create")
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
                    // 유효성 검사 통과 못했을 시
                    !isValidText -> {
                        binding.tvNicknameCheck.setHint("특수문자와 공백은 사용할 수 없습니다")
                        binding.tvNicknameCheck.setHintTextColor(ContextCompat.getColor(this, R.color.red))
                        binding.etvNickname.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.red)
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
                        binding.etvNickname.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.inValid)
                        return@onEach
                    }
                    // 입력된 텍스트가 2글자 미만(유효범위 벗어남)
                    inputText.length < 2 -> {
                        binding.tvNicknameCheck.setHint("2글자 이상 8글자 이하만 가능합니다")
                        binding.tvNicknameCheck.setHintTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.inValid
                            )
                        )
                        binding.etvNickname.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.inValid)
                        return@onEach
                    }
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
                        binding.etvNickname.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.valid)
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
                    val result = loginRepo.checkDuplicateNickname(nickname)
                    when (result) {
                        // 값이 참이라면 중복된 값이 있다는 뜻이므로 존재하는 닉네임임
                        true -> {
                            binding.tvNicknameCheck.setHint("이미 존재하는 닉네임입니다")
                            binding.tvNicknameCheck.setHintTextColor(ContextCompat.getColor(this, R.color.inValid))
                        }
                        // 아니라면 닉네임이 없는거니까 사용할 수 있는 닉네임임
                        else -> {
                            binding.tvNicknameCheck.setHint("사용할 수 있는 닉네임입니다")
                            binding.tvNicknameCheck.setHintTextColor(ContextCompat.getColor(this, R.color.valid))
                            binding.btnSetUser.isEnabled = true
                        }
                    }
                }

            }
            .launchIn(lifecycleScope)
    }


    private fun isValidNickname(nickname: String): Boolean {
        val onlyNumber = "[0-9]".toRegex()
        val validCharsRegex = "^[a-zA-Z가-힣0-9]+$".toRegex()

        return when {
            nickname.isEmpty() || nickname.length < 2 ||nickname.length > 8 -> false
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
        if (nickname.isNotEmpty()) {
            try {
                lifecycleScope.launch {
                    val result = loginRepo.setUser(
                        UserInfo(
                            "",
                            nickname,
                            "",
                            LocalDateTime.now().toString()
                        )
                    )

                    if (result) {
                        preference.nickName = nickname
                        Intent(this@LoginActivity, MainActivity::class.java).run {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(this)
                        }
                        finish()
                    }
                }

            } catch (e: Exception) {
                Log.d("error", e.message.toString())
                Toast.makeText(this@LoginActivity, e.message, LENGTH_SHORT).show()
            }
        }
    }
}