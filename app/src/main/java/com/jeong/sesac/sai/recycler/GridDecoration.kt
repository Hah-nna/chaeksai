package com.jeong.sesac.sai.recycler

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridDecoration(val context : Context) : RecyclerView.ItemDecoration() {

    private fun dpToPx(dp: Float) =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            context.resources.displayMetrics
        )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val spacing = dpToPx(16F).toInt()

        outRect.top = spacing
        outRect.right = spacing
        outRect.bottom = spacing
        outRect.left = spacing
    }
}