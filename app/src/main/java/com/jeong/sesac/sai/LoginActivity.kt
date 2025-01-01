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


        binding.etvNickname.textChanges()
            .debounce(500L)
            .onEach { char ->
                // 0.5초마다 debounce하는데요
                // 1. 특수문자, 숫자로만 되어있는지(숫자로 안 되어있어야함), 2글자 이상인지 확인
                // 2. 1의 경우가 다 참이라면 etvNickname.getText()를 파베에 쿼리로 조회해서
                // 중복되는게 있는지 없는지 확인
                // --- 여기까지 앱 사용하러가기는 Enabled = false여야함
                //
            }
            .launchIn(lifecycleScope)


        /*
        *
        * 닉네임 받아서 유저생성 및 preference에 저장
        * */
        with(binding) {
            btnSetUser.clicks().throttleFirst(throttleTime).onEach {
                val nickname = etvNickname.text.toString()
                setUser(nickname)
            }.launchIn(lifecycleScope)
        }
    }


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