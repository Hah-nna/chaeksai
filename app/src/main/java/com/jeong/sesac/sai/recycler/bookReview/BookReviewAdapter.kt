package com.jeong.sesac.sai.recycler.bookReview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import coil3.size.Scale
import com.jeong.sesac.feature.model.BookReviewWithUser
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.ItemBookReviewBinding
import com.jeong.sesac.sai.util.toTimeConverter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class BookReviewAdapter(
    val userId: String,
    val lifecycleScope: LifecycleCoroutineScope,
    val callback : (BookReviewWithUser, Boolean) -> Unit
) : ListAdapter<BookReviewWithUser, BookReviewAdapter.BookReviewViewHolder>(DiffMap) {

    inner class BookReviewViewHolder(val binding: ItemBookReviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object DiffMap : DiffUtil.ItemCallback<BookReviewWithUser>() {
        override fun areItemsTheSame(oldItem: BookReviewWithUser, newItem: BookReviewWithUser) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: BookReviewWithUser, newItem: BookReviewWithUser) =
            oldItem.id == newItem.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookReviewViewHolder {
        return BookReviewViewHolder(ItemBookReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BookReviewViewHolder, position: Int) {
        val bookReview = currentList[position]
        holder.binding.apply {

            ivProfile.load(bookReview.userInfo.profile) {
                crossfade(true)
                scale(Scale.FILL)
                fallback(R.drawable.ic_default_profile)
                error(R.drawable.ic_default_profile)
            }
            tvNickname.text = bookReview.userInfo.nickName
            tvContent.text = bookReview.content
            tvTime.text = bookReview.createdAt.toTimeConverter()
            rating.rating = bookReview.score

            ivMenu.clicks().onEach {
                val isReviewUser = bookReview.userInfo.id == userId
                callback(bookReview, isReviewUser)
            }.launchIn(lifecycleScope)
        }
    }
}