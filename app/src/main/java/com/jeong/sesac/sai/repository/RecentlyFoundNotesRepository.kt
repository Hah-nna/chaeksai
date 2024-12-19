package com.jeong.sesac.sai.repository

import com.jeong.sesac.sai.repository.interfaces.RecentlyFoundNotesInterface

class RecentlyFoundNotesRepository : RecentlyFoundNotesInterface {

    override fun getRecentNotes() {
        TODO("파베에서 리스트 받아오기(최신순)")
    }

    override fun getNotesLikesDesc() {
        TODO("파베에서 리스트 받아오기(좋아요높은순)")
    }

    override fun getNotesLikesAsc() {
        TODO("파베에서 리스트 받아오기(좋아요낮은순)")
    }
}