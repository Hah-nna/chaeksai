package com.jeong.sesac.feature.model

import java.util.Date


data class Like (
    val id: String,
    val userId: String,
    val noteId: String,
    val isLiked: Boolean,
    val createdAt: Long
)

data class PlaceInfo(
    val id: String,
    val address: String,
    val phone: String,
    val place: String,
    val placeURL: String,
    val lat: String,
    val lng: String
)

data class Note(
    val id: String = "",
    val userId: String = "",
    val image: String = "",
    val title: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val libraryName: String = "",
    val content: String = "",
    val likes: Int = 0,
)

data class NoteWithUser(
    val id: String,
    val userInfo: UserInfo,
    val image: String,
    val title: String,
    val createdAt: Long,
    val libraryName: String,
    val content: String,
    val likes: Int,
)

data class UserInfo (
    val id: String,
    val profile: String,
    val nickName: String
)

data class Comment(
    val id: String,
    val userId: String,
    val noteId: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class CommentWithUser(
    val id: String = "",
    val userInfo: UserInfo = UserInfo(
        id = "",
        profile = "",
        nickName = ""
    ),
    val content: String = "",
    val createdAt: Long = 0L
)

data class User(
    val id: String,
    val profile: String,
    val nickname: String,
    val created_at: Long,
    val provider_info: ProviderType = ProviderType.KAKAO
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "nickname" to nickname,
            "profile" to profile,
            "created_at" to created_at,
            "provider_info" to provider_info.toString()
        )
    }

}


enum class ProviderType {
    KAKAO,
    NAVER,
    GOOGLE
}