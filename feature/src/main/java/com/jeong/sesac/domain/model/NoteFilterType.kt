package com.jeong.sesac.domain.model


sealed class NoteFilterType {
    data class ByLibrary(val libraryName: String): NoteFilterType()
    data class ThisWeek(val sortOrder: SortOrder): NoteFilterType()
    data class ByLikes(val ascending: Boolean): NoteFilterType()
    data class ByCreatedAt(val ascending: Boolean): NoteFilterType()
    data object MyNotes: NoteFilterType()
    data object MyLikedNotes: NoteFilterType()
}

enum class SortOrder {
    LATEST,      // 최신순
    LIKES_DESC,  // 좋아요 높은순
    LIKES_ASC    // 좋아요 낮은순
}