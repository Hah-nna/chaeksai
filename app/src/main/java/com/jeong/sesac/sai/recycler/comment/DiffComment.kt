package com.jeong.sesac.sai.recycler.comment

import androidx.recyclerview.widget.DiffUtil
import com.jeong.sesac.sai.model.CommentUI

class DiffComment : DiffUtil.ItemCallback<CommentUI>() {

    override fun areItemsTheSame(oldItem: CommentUI, newItem: CommentUI): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: CommentUI, newItem: CommentUI): Boolean {
        return oldItem.id == newItem.id
    }
}