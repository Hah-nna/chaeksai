package com.jeong.sesac.sai.recycler.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.jeong.sesac.feature.model.Comment
import com.jeong.sesac.sai.databinding.ItemCommentBinding
import com.jeong.sesac.sai.model.CommentUI

class CommentAdapter(val callback : (CommentUI) -> Unit) : ListAdapter<CommentUI, CommentViewHolder>(DiffComment()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val commentList = currentList[position]
        holder.binding.apply {
            ivProfile.setImageResource(commentList.profile.profile)
            tvNickname.text = commentList.nickname.nickname
            tvContent.text = commentList.content
            tvTime.text = commentList.createdAt
        }
    }
}