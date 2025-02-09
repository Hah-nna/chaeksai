package com.jeong.sesac.data.datasource

import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jeong.sesac.feature.model.Like
import com.jeong.sesac.feature.model.Note
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.feature.model.User
import com.jeong.sesac.feature.model.UserInfo
import kotlinx.coroutines.tasks.await
import java.util.Date


class FireBaseDataSourceImpl(private val storageDataSource: FireBaseStorageDataSource) :
    FireBaseDataSource {
    private val userCollectionRef = Firebase.firestore.collection("users")
    private val noteCollectionRef = Firebase.firestore.collection("notes")
    private val likeCollectionRef = Firebase.firestore.collection("likes")

    override suspend fun createUser(userInfo: User): Boolean {
        try {
            val docRef = userCollectionRef.add(userInfo.toMap()).await()
            val docRefId = docRef.id

            userCollectionRef.document(docRefId)
                .update("id", docRefId)
                .await()

            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun getIdByNickname(nickname: String): String? {
        return try {
            val result = userCollectionRef.whereEqualTo("nickname", nickname).get().await()
            result.documents.firstOrNull()?.id
        } catch (e: Exception) {
            null
        }
    }


    override suspend fun getDuplicateNickname(nickname: String): Boolean {
        try {
            val result = userCollectionRef
                .whereEqualTo("nickname", nickname)
                .get()
                .await()

            return result.size() > 0
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun createNote(note: Note, nickname: String): Boolean {
        try {
            val imgUrl =
                if (note.image.isNotEmpty()) storageDataSource.createImg(Uri.parse(note.image)) else ""

            val noteWithImg = note.copy(image = imgUrl)
            val userId = getIdByNickname(nickname)
            val noteDocRef = noteCollectionRef.add(noteWithImg).await()
            val noteDocRefId = noteDocRef.id

            noteCollectionRef.document(noteDocRefId)
                .update(
                    mapOf(
                        "id" to noteDocRefId,
                        "userId" to userId,
                        "createdAt" to note.createdAt
                    )
                ).await()

            return true
        } catch (e: Exception) {
            Log.e(" createNote err", e.message.toString())
            return false
        }
    }

    override suspend fun getNoteList(): List<Note> {
        return try {
            noteCollectionRef
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents.mapNotNull { it.toObject(Note::class.java) }

        } catch (e: Exception) {
            Log.e("getNotelist error!!!", "${e.message}")
            emptyList()
        }
    }

    override suspend fun getUserInfo(userId: String): UserInfo {
        return try {
            val userDoc = userCollectionRef.document(userId).get().await()
            Log.d("DEBUG", "Document data: ${userDoc.data}")
            UserInfo(
                id = userDoc.id,
                nickName = userDoc["nickname"].toString(),
                profile = userDoc["profile"].toString()
            )


        } catch (e: Exception) {
            Log.d("error!!!!!!", "${e.message}")
            throw e
        }
    }

    override suspend fun getMyLikedNotes(userId: String): List<String> {
        return try {
            val likedNotes = likeCollectionRef.whereEqualTo("userId", userId)
                .whereEqualTo("isLiked", true)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            likedNotes.documents.mapNotNull { doc ->
                doc.toObject(Like::class.java)?.noteId
            }

        } catch (e: Exception) {
            Log.e("likedNote error", "${e.message}")
            emptyList()
        }
    }

    override suspend fun getLibraryNotes(libraryName: String): Result<List<NoteWithUser>> {
        return runCatching {
            val libraryNote = noteCollectionRef.whereEqualTo("libraryName", libraryName)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            libraryNote.documents.mapNotNull { doc ->
                Log.d("도서관노트", "${doc.data}")
                doc.toObject(NoteWithUser::class.java)
            }
        }.onFailure {
            e ->
            Log.e("도서관별 쪽지 가져오기 error", "${e.message}")
        }

    }

    override suspend fun getNote(noteId: String): Result<NoteWithUser> {
        return runCatching {
            val selectedNote = noteCollectionRef.whereEqualTo("id", noteId)
                .get()
                .await()

            Log.d("selectedNote", "${selectedNote}")

            selectedNote.documents.mapNotNull { doc ->
                doc.toObject(NoteWithUser::class.java)
            }.single()
        }.onFailure { e ->
            Log.e("쪽지 가져오기 error", "${e.message}")
        }
    }
}