package com.jeong.sesac.domain.repository

import com.jeong.sesac.feature.model.Comment
import com.jeong.sesac.feature.model.CommentWithUser

interface ICommentRepository {
    suspend fun createComment(userId: String, noteId: String, comment: Comment): Boolean
    suspend fun getComments(userId: String, noteId: String): Result<List<CommentWithUser>>
    suspend fun updateComment(noteId: String, commentId: String, content: String): Result<Unit>
    suspend fun deleteComment(noteId: String, commentId: String): Result<Unit>
}