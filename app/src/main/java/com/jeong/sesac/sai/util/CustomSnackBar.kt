package com.jeong.sesac.sai.util

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.jeong.sesac.sai.R

class CustomSnackBar {
    companion object {
        fun snackBar(view: View, context: Context, message: String) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(context, R.color.accent_text))
                .setTextColor(ContextCompat.getColor(context, R.color.white))
                .setActionTextColor(ContextCompat.getColor(context, R.color.red))
                .show()
        }
    }
}