package com.jeong.sesac.sai.recycler.libraryNote

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.ListAdapter
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import coil3.size.Scale
import coil3.size.Size
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.ItemLibraryNoteBinding
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.util.toTimeConverter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class LibraryNotesAdapter(
    val userId: String,
    val lifecycleScope: LifecycleCoroutineScope,
    val callBack: (NoteWithUser) -> Unit,
) : ListAdapter<NoteWithUser, LibraryNoteViewHolder>(DiffLibraryNote()){

    private val likedNotesState = mutableMapOf<String, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryNoteViewHolder {
        return LibraryNoteViewHolder(
            ItemLibraryNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LibraryNoteViewHolder, position: Int) {
        val libraryNote = currentList[position]


        holder.binding.apply {

            val initialLikeState = libraryNote.likes.contains(userId)
            val currentLikeState = likedNotesState[libraryNote.id]

            val isLiked = if(currentLikeState != null) currentLikeState else initialLikeState

            val likeCount = if (isLiked != initialLikeState) {
                libraryNote.likes.size + (if (isLiked) 1 else -1)
            } else {
                libraryNote.likes.size
            }


            ivProfile.load(libraryNote.userInfo.profile) {
                crossfade(true)
                scale(Scale.FILL)
                fallback(R.drawable.ic_default_profile)
                error(R.drawable.ic_default_profile)
            }

            ivImg.load(libraryNote.image) {
                crossfade(true)
                size(Size.ORIGINAL)
                scale(Scale.FILL)
                fallback(R.drawable.ic_default_profile)
                error(R.drawable.ic_default_profile)
            }

            ivLike.setImageResource(
                if(isLiked) R.drawable.ic_filled_heart
                else R.drawable.ic_empty_heart
            )

            tvNickname.text = libraryNote.userInfo.nickName
            tvLibraryName.text = libraryNote.libraryName
            tvTitle.text = libraryNote.title
            tvLikeCount.text = likeCount.toString()
            tvTime.text = libraryNote.createdAt.toTimeConverter()
            root.clicks().throttleFirst(throttleTime).onEach {
                callBack(libraryNote)
            }.launchIn(lifecycleScope)
            ivLike.clicks().throttleFirst(throttleTime).onEach {

                val newLikeState = !isLiked

                likedNotesState[libraryNote.id] = newLikeState
                notifyItemChanged(position)
                Log.d("newLikeState", "${newLikeState}")
            }.launchIn(lifecycleScope)
        }
    }

    fun getTotalLikes() = likedNotesState
    fun clearTotalLikes() = likedNotesState.clear()
}