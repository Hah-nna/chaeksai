package com.jeong.sesac.domain.repository

import com.jeong.sesac.feature.model.Comment
import com.jeong.sesac.feature.model.CommentWithUser

interface ICommentRepository {
    suspend fun createComment(nickname: String, noteId: String, comment: Comment): Boolean
    suspend fun getComments(nickname: String, noteId: String): List<CommentWithUser>
}