package com.jeong.sesac.sai.recycler.book

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
import com.jeong.sesac.feature.model.BookInfo
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.ItemBookBinding
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class BookAdapter(
    val lifecycleScope: LifecycleCoroutineScope,
    val onBookDetailCallback: (BookInfo) -> Unit
) : ListAdapter<BookInfo, BookAdapter.BookViewHolder>(DiffMap) {

    inner class BookViewHolder(val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object DiffMap : DiffUtil.ItemCallback<BookInfo>() {
        override fun areItemsTheSame(oldItem: BookInfo, newItem: BookInfo) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: BookInfo, newItem: BookInfo) =
            oldItem.id == newItem.id
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = currentList[position]

        holder.binding.apply {
            ivImg.load(book.book_img) {
                crossfade(true)
                crossfade(300)
                scale(Scale.FILL)
                fallback(R.drawable.no_img)
                error(R.drawable.no_img)
            }
            tvBookTitle.text = book.title
            rating.rating = 5f
            root.clicks().throttleFirst(throttleTime).onEach {
                onBookDetailCallback(book)
            }.launchIn(lifecycleScope)
        }
    }

}