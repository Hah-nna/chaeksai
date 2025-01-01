package com.jeong.sesac.sai

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jeong.sesac.datamodule.datasource.FireBaseDataSourceImpl
import com.jeong.sesac.datamodule.repository.LoginRepositoryImpl
import com.jeong.sesac.domain.model.UserInfo
import com.jeong.sesac.sai.databinding.ActivityLoginBinding
import com.jeong.sesac.sai.databinding.FragmentLoginByNicknameBinding
import com.jeong.sesac.sai.ui.login.LoginFragment
import com.jeong.sesac.sai.util.AppPreferenceManager
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import java.time.LocalDateTime

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginRepo = LoginRepositoryImpl(FireBaseDataSourceImpl())
    private val preference by lazy { AppPreferenceManager.getInstance(this)}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("LoginActivity create", "create")
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        with(binding) {
            btnSetUser.clicks().throttleFirst(throttleTime).onEach {
                val nickname = etvNickname.text.toString()
                if(nickname.isNotEmpty()) {
                    try {
                        lifecycleScope.launch {
                        val result = loginRepo.setUser(UserInfo(
                                "",
                                nickname,
                                "",
                                LocalDateTime.now().toString()
                            ))

                            if(result) {
                                preference.nickName = nickname
                                Intent(this@LoginActivity, MainActivity::class.java).run {
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(this)
                                }
                                    finish()

                            }

                        }

                    } catch (e : Exception) {
                        Log.d("error", e.message.toString())
                            Toast.makeText(this@LoginActivity, e.message, LENGTH_SHORT).show()
                    }
                }
            }.launchIn(lifecycleScope)
        }
    }

    override fun onResume() {
        super.onResume()


    }

    override fun onStop() {
        super.onStop()
        Log.d("LoginActivity onStop", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LoginActivity onDestroy", "onDestroy")
    }

}