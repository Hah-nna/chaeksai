package com.jeong.sesac.data.datasource

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jeong.sesac.feature.model.Like
import com.jeong.sesac.feature.model.Note
import com.jeong.sesac.feature.model.NoteWithUser
import com.jeong.sesac.feature.model.User
import com.jeong.sesac.feature.model.UserInfo
import kotlinx.coroutines.tasks.await


class FireBaseDataSourceImpl(private val storageDataSource: FireBaseStorageDataSource) :
    FireBaseDataSource {
    private val userCollectionRef = Firebase.firestore.collection("users")
    private val noteCollectionRef = Firebase.firestore.collection("notes")
    private val likeCollectionRef = Firebase.firestore.collection("likes")

    override suspend fun createUser(userInfo: User): Result<String> {
        return runCatching {
            val docRef = userCollectionRef.add(userInfo.toMap()).await()
            val docRefId = docRef.id

            userCollectionRef.document(docRefId)
                .update("id", docRefId)
                .await()
            docRefId
        }.onFailure { e ->
            Log.d("login error!", "${e.message}, ${e.cause}")
            throw Error(e.message, e.cause)
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


    override suspend fun getDuplicateNickname(nickname: String): Result<Boolean> {
        return runCatching {
            val result = userCollectionRef
                .whereEqualTo("nickname", nickname)
                .get()
                .await()
            result.size() > 0
        }.onFailure { e ->
            Log.d("login error!", "${e.message}, ${e.cause}")
            throw Error(e.message, e.cause)
        }
    }

    override suspend fun createNote(note: Note): Result<Boolean> {
        return runCatching {
            val noteDocRef = noteCollectionRef.add(note).await()
            val noteDocRefId = noteDocRef.id
            val imgUri = if (note.image.isNotEmpty()) storageDataSource.createImg(
                    Uri.parse(note.image),
                    noteDocRefId
                ) else ""
            noteCollectionRef.document(noteDocRefId)
                .update(
                    mapOf(
                        "id" to noteDocRefId,
                        "userId" to note.userId,
                        "image" to imgUri,
                        "createdAt" to note.createdAt
                    )
                ).await()
            true
        }.onFailure { e ->
            Log.d("create note error", "${e.message}, ${e.cause}")
        }
    }

    override suspend fun getNoteList(): Result<List<Note>> {
        return runCatching {
            noteCollectionRef
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
                .documents.mapNotNull { it.toObject(Note::class.java) }
        }.onFailure {e ->
                Log.e("getNotelist error!!!", "${e.message}")
        }
    }

    override suspend fun getUserInfo(userId: String): UserInfo {
        return try {
            Log.d("getUserInfo userId", "${userId}")
            val userDoc = userCollectionRef.document(userId).get().await()
            Log.d("DEBUG", "Document data: ${userDoc.data}")
            UserInfo(
                id = userId,
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
                Log.d("도서관별 노트 데이터", "${doc.data}")
                doc.toObject(NoteWithUser::class.java)


            }
        }.onFailure { e ->
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
                val noteData = doc.toObject(NoteWithUser::class.java)
                Log.d("Firebase Debug", "문서 데이터: ${doc.data}")
                Log.d("Firebase Debug", "변환된 노트 데이터: $noteData")
               val note = doc.toObject(Note::class.java)

                note?.let {

                    val userInfo = try {
                        getUserInfo(note.userId)
                    } catch (e: Exception) {
                       Log.d("error", "${e.message}")
                    }

                    NoteWithUser(
                        id = it.id,
                        userInfo = userInfo as UserInfo,
                        image = it.image,
                        title = it.title,
                        content = it.content,
                        createdAt = it.createdAt,
                        libraryName = it.libraryName,
                        likes = it.likes
                    )
                }
            }.single()
        }.onFailure { e ->
            Log.e("쪽지 가져오기 error", "${e.message}")
        }
    }

    override suspend fun updateNote(noteId: String, note: Note): Result<Unit> {
        return runCatching<FireBaseDataSourceImpl, Unit> {

            val updates = mutableMapOf<String, Any>()

            if (note.title.isNotEmpty()) updates["title"] = note.title
            if (note.content.isNotEmpty()) updates["content"] = note.content
            if (note.image.isNotEmpty()) updates["image"] = note.image

            updates["createdAt"] = System.currentTimeMillis()

            noteCollectionRef.document(noteId)
                .update(updates)
                .await()
        }.onFailure { e ->
            Log.e("note update 실패", "${e.message}")
        }
    }

    override suspend fun deleteNote(noteId: String): Result<Unit> {
        return runCatching {
            storageDataSource.deleteImg(noteId).onSuccess {
                noteCollectionRef.document(noteId)
                    .delete()
                    .await()
            }
        }.map { Unit }
            .onFailure { e ->
                Log.e("delete 실패", "${e.message}")
            }
    }
}