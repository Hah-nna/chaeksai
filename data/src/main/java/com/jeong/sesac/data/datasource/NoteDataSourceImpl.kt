package com.jeong.sesac.data.datasource

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jeong.sesac.feature.model.Note
import kotlinx.coroutines.tasks.await

class NoteDataSourceImpl(private val storageDataSource: FireBaseStorageDataSource) : NoteDataSource {
    private val noteCollectionRef = Firebase.firestore.collection("notes")


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
        }.onFailure { e ->
            Log.e("getNotelist error!!!", "${e.message}")
            throw Exception("쪽지를 가져오는데 실패했습니다 ${e.message}")
        }
    }


    override suspend fun getLibraryNotes(libraryName: String): Result<List<Note>> {
        return runCatching {
            val libraryNote = noteCollectionRef.whereEqualTo("libraryName", libraryName)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            libraryNote.documents.mapNotNull {
                Log.d("도서관별 노트 데이터", "${it.data}")
                it.toObject(Note::class.java)

            }
        }.onFailure { e ->
            Log.e("도서관별 쪽지 가져오기 error", "${e.message}")
        }

    }

    override suspend fun getNote(noteId: String): Result<Note> {
        return runCatching {
            val selectedNote = noteCollectionRef.whereEqualTo("id", noteId)
                .get()
                .await()

            Log.d("selectedNote", "$selectedNote")

            selectedNote.documents.mapNotNull { doc ->
                Log.d("Firebase Debug", "문서 데이터: ${doc.data}")
                doc.toObject(Note::class.java)
            }.single()
        }.onFailure { e ->
            Log.e("쪽지 가져오기 error", "${e.message}")
            throw Exception("선택한 쪽지내용 가져오기 실패 ${e.message}")
        }
    }

    override suspend fun updateNote(noteId: String, note: Note): Result<Unit> {
        return runCatching {
            val updates = mutableMapOf<String, Any>()

            if (note.title.isNotEmpty()) updates["title"] = note.title
            if (note.content.isNotEmpty()) updates["content"] = note.content
            if (note.image.isNotEmpty()) updates["image"] = note.image

            updates["createdAt"] = System.currentTimeMillis()

            noteCollectionRef.document(noteId)
                .update(updates)
                .await()
        }.map { Unit }
            .onFailure { e ->
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

    override suspend fun toggleLike(noteId: String, userId: String): Result<Boolean> {
        return runCatching {
            val noteRef = noteCollectionRef.document(noteId)
            Firebase.firestore.runTransaction { transaction ->
                val noteSnapshot = transaction.get(noteRef)
                val note = noteSnapshot.toObject(Note::class.java)

                note?.let {
                    val likeList = note.likes.toMutableList()
                    val isLiked = likeList.contains(userId)

                    if (isLiked) likeList.remove(userId) else likeList.add(userId)

                    transaction.update(noteRef, "likes", likeList)
                    !isLiked
                } ?: throw Exception("해당 쪽지를 찾을 수 없습니다")
            }.await()
        }.onFailure {
            Log.e("toggle 라이크 실패", "${it.message}, ${it.cause}")
            throw Exception("좋아요 토글 실패")
        }
    }


}