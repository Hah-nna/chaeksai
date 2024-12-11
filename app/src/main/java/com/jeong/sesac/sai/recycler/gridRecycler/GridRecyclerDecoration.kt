package com.jeong.sesac.sai.recycler.gridRecycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridRecyclerDecoration(
    private val spanCount : Int,
    private val spacing: Int
) : RecyclerView.ItemDecoration() {

    /** 개별 항목 데코레이션시 호출*/
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val position = parent.getChildLayoutPosition(view)
        val column = position % spanCount

        if(column == 0) {
            outRect.right = spacing / 2
        } else {
            outRect.left = spacing / 2
        }

        outRect.bottom = spacing
    }
}