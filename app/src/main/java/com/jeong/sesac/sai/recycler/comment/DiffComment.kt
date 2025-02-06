package com.jeong.sesac.sai.recycler.comment

import androidx.recyclerview.widget.DiffUtil
import com.jeong.sesac.feature.model.CommentWithUser
import com.jeong.sesac.sai.model.CommentUI

class DiffComment : DiffUtil.ItemCallback<CommentWithUser>() {

    override fun areItemsTheSame(oldItem: CommentWithUser, newItem: CommentWithUser): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: CommentWithUser, newItem: CommentWithUser): Boolean {
        return oldItem.id == newItem.id
    }
}