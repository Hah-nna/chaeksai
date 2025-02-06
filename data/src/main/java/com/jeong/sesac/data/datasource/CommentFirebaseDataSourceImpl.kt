package com.jeong.sesac.data.datasource

import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jeong.sesac.feature.model.Comment
import com.jeong.sesac.feature.model.CommentWithUser
import com.jeong.sesac.feature.model.Note
import kotlinx.coroutines.tasks.await

class CommentFirebaseDataSourceImpl(private val firebaseDataSource: FireBaseDataSourceImpl) : CommentFirebaseDataSource {
    private val noteCollectionRef = Firebase.firestore.collection("notes")


    override suspend fun createComment(nickname: String, noteId: String, comment: Comment) : Boolean {
       return runCatching {
            val commentRef = noteCollectionRef
                .document(noteId)
                .collection("comments")
                .add(comment)
                .await()

            noteCollectionRef
                .document(noteId)
                .collection("comments")
                .document(commentRef.id)
                .update("id", commentRef.id)
                .await()

            val userId = firebaseDataSource.getIdByNickname(nickname)

            noteCollectionRef
                .document(noteId)
                .collection("comments")
                .document(commentRef.id)
                .update("userId", userId)
                .await()
           true
        }.getOrElse { e ->
           Log.e("코멘트생성에러", "코멘트생성에러: ${e.message}")
           false
       }
    }

    override suspend fun getComments(nickname: String, noteId: String): List<CommentWithUser> {
        return runCatching {
           noteCollectionRef
                .document(noteId)
                .collection("comments")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
               .documents.mapNotNull { it ->

                   val userId = firebaseDataSource.getIdByNickname(nickname)

                   val comment = it.toObject(CommentWithUser::class.java)
                   comment?.let {
                      val userInfo = firebaseDataSource.getUserInfo(userId!!)
                   Log.d("userinfo", "${ userInfo.id }, ${userInfo.nickName}, ${userInfo.profile}")

                       CommentWithUser(
                           id = it.id,
                           userInfo = userInfo,
                           content = it.content,
                           createdAt = it.createdAt
                       )
                   }
               }

        }.getOrElse { e ->
            Log.e("코멘트가져오기실패", "코멘트가져오기실패: ${e.message}")
            emptyList()
        }
    }

    override suspend fun UpdateComment() {
        TODO("Not yet implemented0")
    }

    override suspend fun DeleteComment() {
        TODO("Not yet implemented")
    }
}