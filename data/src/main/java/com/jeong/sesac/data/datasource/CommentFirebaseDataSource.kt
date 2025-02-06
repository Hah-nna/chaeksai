package com.jeong.sesac.data.datasource

import com.jeong.sesac.feature.model.Comment
import com.jeong.sesac.feature.model.CommentWithUser

interface CommentFirebaseDataSource {
    suspend fun createComment(nickname: String, noteId: String, comment: Comment): Boolean
    suspend fun getComments(nickname: String, noteId: String): List<CommentWithUser>
    suspend fun DeleteComment()
    suspend fun UpdateComment()
}