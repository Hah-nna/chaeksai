package com.jeong.sesac.sai.repository.interfaces

interface RecentlyFoundNotesInterface {
    // 초기에 보여줄 리스트 및 최신순 리스트
    fun getRecentNotes()
    // 좋아요 높은 순
    fun getNotesLikesDesc()
    // 좋아요 낮은 순
    fun getNotesLikesAsc()
}