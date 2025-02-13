package com.jeong.sesac.sai

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil3.ImageLoader
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.load
import coil3.request.crossfade
import com.jeong.sesac.sai.util.AppPreferenceManager

class SplashActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgLoader = ImageLoader.Builder(this@SplashActivity)
            .components {
                if (SDK_INT >= 28) {
                    add(AnimatedImageDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }.build()

        findViewById<AppCompatImageView>(R.id.logo).load(R.drawable.sp_logo, imgLoader) {
            crossfade(true)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("SplashActivity create", "create")
        handler.postDelayed({
            val preferences = AppPreferenceManager.getInstance(this)
            if (preferences.nickName.isEmpty()) {
                Intent(this@SplashActivity, LoginActivity::class.java).run {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(this)
                }
                finish()
            } else {
                Intent(this@SplashActivity, MainActivity::class.java).run {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(this)
                }
                finish()
            }
        }, 1500)
    }
}