package com.jeong.sesac.sai.recycler.newNote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import coil3.load
import coil3.request.crossfade
import coil3.size.Scale
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.sai.databinding.ItemNewNoteBinding
import com.jeong.sesac.sai.model.LibraryNoteUI

class NewNoteAdapter(val callBack: (NoteWithUser) -> Unit) :
    ListAdapter<NoteWithUser, NewNoteViewHolder>(DiffUtilNewNote()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewNoteViewHolder {
        return NewNoteViewHolder(
            ItemNewNoteBinding.inflate(
                /**
                 * LayoutInflater
                 * xml을 실제 View 객체로 변환하는 시스템 서비스
                 *
                 * LayoutInflater.from(parent.context) :
                 * 리사이클러뷰의 context(parent.context)를 가져와서 LayoutInflater의 인스턴스를 생성하겠음
                 *
                 *
                 * parent, false :
                 * 리사이클러뷰 아이템(ItemRecentlyFoundNoteBinding)이 붙을 부모뷰(RecyclerView)를 지정함
                 * false 로 설정해서 ItemRecentlyFoundNoteBinding을 parent에 바로 붙이지 않음
                 *
                 * */
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NewNoteViewHolder, position: Int) {
        val noteList = currentList[position]
        holder.binding.apply {
            itemRecentlyFoundNoteImg.load(noteList.image) {
                crossfade(true)
                scale(Scale.FILL)
            }
            root.setOnClickListener {
                callBack(noteList)
            }
        }
    }

}