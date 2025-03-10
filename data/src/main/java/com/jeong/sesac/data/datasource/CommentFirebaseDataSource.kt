package com.jeong.sesac.data.datasource

import com.jeong.sesac.feature.model.Comment

interface CommentFirebaseDataSource {
    suspend fun createComment(userId: String, noteId: String, comment: Comment): Boolean
    suspend fun getComments(userId: String, noteId: String): Result<List<Comment>>
    suspend fun updateComment(noteId: String, commentId: String, content: String) : Result<Unit>
    suspend fun deleteComment(noteId: String, commentId: String): Result<Unit>
}