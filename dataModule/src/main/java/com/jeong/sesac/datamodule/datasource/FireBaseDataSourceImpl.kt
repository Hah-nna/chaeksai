package com.jeong.sesac.datamodule.datasource

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jeong.sesac.domain.model.UserInfo
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await

class FireBaseDataSourceImpl() : FireBaseDataSource {
    private val userCollectionRef = Firebase.firestore.collection("users_collection")

    override suspend fun createUser(userInfo: UserInfo): Boolean {
        try {
            val docRef = userCollectionRef.add(userInfo.toMap()).await()
            val docRefId = docRef.id
            Log.d("유저생성 data", docRef.id)

            userCollectionRef.document(docRefId)
                .update("id", docRefId)
                .await()
            return true
        } catch (e: Exception) {
            Log.e("FireBaseDataSourceImpl.createUser() is fail", e.message.toString())
            return false
        }
    }
}