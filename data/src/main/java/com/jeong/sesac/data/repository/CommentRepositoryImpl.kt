package com.jeong.sesac.data.repository

import com.jeong.sesac.data.datasource.CommentFirebaseDataSourceImpl
import com.jeong.sesac.domain.repository.ICommentRepository
import com.jeong.sesac.feature.model.Comment
import com.jeong.sesac.feature.model.CommentWithUser
import java.util.Date

class CommentRepositoryImpl(private val commentDataSource: CommentFirebaseDataSourceImpl) : ICommentRepository {

    override suspend fun createComment(nickname: String, noteId: String, comment: Comment):
            Boolean {
        val userComment = Comment(
            id = "",
            userId = "",
            noteId = noteId,
            content = comment.content,
            createdAt = System.currentTimeMillis()
        )

        return commentDataSource.createComment(nickname, noteId, userComment)
    }

    override suspend fun getComments(nickname: String, noteId: String): Result<List<CommentWithUser>> {
        return commentDataSource.getComments(nickname, noteId)
    }

    override suspend fun updateComment(noteId: String, commentId: String, content: String): Result<Unit> {
        return commentDataSource.updateComment(noteId, commentId, content)
    }

    override suspend fun deleteComment(noteId: String, commentId: String): Result<Unit> {
        return commentDataSource.deleteComment(noteId, commentId)
    }
}