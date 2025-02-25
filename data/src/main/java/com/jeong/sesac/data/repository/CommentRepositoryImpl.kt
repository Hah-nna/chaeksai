package com.jeong.sesac.data.repository

import com.jeong.sesac.data.datasource.CommentFirebaseDataSourceImpl
import com.jeong.sesac.domain.repository.ICommentRepository
import com.jeong.sesac.feature.model.Comment
import com.jeong.sesac.feature.model.CommentWithUser
import com.jeong.sesac.feature.model.UserInfo

class CommentRepositoryImpl(private val commentDataSource: CommentFirebaseDataSourceImpl, private val userRepo: UserRepositoryImpl) : ICommentRepository {

    override suspend fun createComment(userId: String, noteId: String, comment: Comment):
            Boolean {
        val userComment = Comment(
            id = "",
            userId = userId,
            noteId = noteId,
            content = comment.content,
            createdAt = System.currentTimeMillis()
        )

        return commentDataSource.createComment(userId, noteId, userComment)
    }

    override suspend fun getComments(userId: String, noteId: String): Result<List<CommentWithUser>> {
        return commentDataSource.getComments(userId, noteId).map { comments ->
            comments.map {comment ->
                    val userInfo = userRepo.getUserInfo(comment.userId)
                    CommentWithUser(
                        id = comment.id,
                        userInfo = UserInfo(
                            id = userInfo.id,
                            profile = userInfo.profile,
                            nickName = userInfo.nickName
                        ),
                        content = comment.content,
                        createdAt = comment.createdAt
                    )
            }
        }.onFailure {
            throw Exception("${it.message}")
        }
    }

    override suspend fun updateComment(noteId: String, commentId: String, content: String): Result<Unit> {
        return commentDataSource.updateComment(noteId, commentId, content)
    }

    override suspend fun deleteComment(noteId: String, commentId: String): Result<Unit> {
        return commentDataSource.deleteComment(noteId, commentId)
    }
}