package com.jeong.sesac.sai.recycler.horizontalRecycler

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RVDecoration(val context : Context) : RecyclerView.ItemDecoration() {


    private fun dpToPx(dp: Float) =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            context.resources.displayMetrics
        )

    override fun getItemOffsets(
        // 각 항목을 출력할 때 필요한 사각형 정보
        // Rect를 사용해 네 방향의 여백 설정, 각 항목의 바탕색 등 설정 가능
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildLayoutPosition(view)
        val itemCount = state.itemCount
        val spacing = dpToPx(16F).toInt()

        when(position) {
            0 -> {
                outRect.left = spacing / 2
                outRect.right = spacing

            }
            itemCount - 1 -> {
                outRect.left = spacing / 2
                outRect.right = spacing
            }
            else -> {
                outRect.left = spacing / 2
                outRect.right = spacing / 2
            }
        }


    }

}