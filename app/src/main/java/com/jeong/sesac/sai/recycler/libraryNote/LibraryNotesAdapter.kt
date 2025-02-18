package com.jeong.sesac.sai.recycler.libraryNote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.ListAdapter
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import coil3.size.Scale
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.R
import com.jeong.sesac.sai.databinding.ItemLibraryNoteBinding
import com.jeong.sesac.sai.util.throttleFirst
import com.jeong.sesac.sai.util.throttleTime
import com.jeong.sesac.sai.util.toTimeConverter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class LibraryNotesAdapter(val lifecycleScope: LifecycleCoroutineScope, val callBack: (NoteWithUser) -> Unit) :
ListAdapter<NoteWithUser, LibraryNoteViewHolder>(DiffLibraryNote()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryNoteViewHolder {
        return LibraryNoteViewHolder(
            ItemLibraryNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LibraryNoteViewHolder, position: Int) {
        val libraryNoteList = currentList[position]
        holder.binding.apply {
            ivProfile.load(libraryNoteList.userInfo.profile) {
                crossfade(true)
                scale(Scale.FILL)
                fallback(R.drawable.ic_default_profile)
                error(R.drawable.ic_default_profile)
            }

            ivImg.load(libraryNoteList.image) {
                crossfade(true)
                scale(Scale.FILL)
                fallback(R.drawable.ic_default_profile)
                error(R.drawable.ic_default_profile)
            }
            tvNickname.text = libraryNoteList.userInfo.nickName
            tvLibraryName.text = libraryNoteList.libraryName
            tvTitle.text = libraryNoteList.title
            tvTime.text = libraryNoteList.createdAt.toTimeConverter()
            root.clicks().throttleFirst(throttleTime).onEach {
                callBack(libraryNoteList)
            }.launchIn(lifecycleScope)
        }

    }
}