package com.jeong.sesac.sai.recycler.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.ListAdapter
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import coil3.size.Scale
import com.jeong.sesac.feature.model.CommentWithUser
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.ItemCommentBinding
import com.jeong.sesac.sai.util.toTimeConverter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class CommentAdapter(
    val userId: String,
    val lifecycleScope: LifecycleCoroutineScope,
    val callback : (CommentWithUser, Boolean) -> Unit
) : ListAdapter<CommentWithUser, CommentViewHolder>(DiffComment()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = currentList[position]
        holder.binding.apply {

            ivProfile.load(comment.userInfo.profile) {
                crossfade(true)
                scale(Scale.FILL)
                fallback(R.drawable.ic_default_profile)
                error(R.drawable.ic_default_profile)
            }
            tvNickname.text = comment.userInfo.nickName
            tvContent.text = comment.content
            tvTime.text = comment.createdAt.toTimeConverter()

            ivMenu.clicks().onEach {
                val isCommentUser = comment.userInfo.id == userId
                callback(comment, isCommentUser)
            }.launchIn(lifecycleScope)
        }
    }
}