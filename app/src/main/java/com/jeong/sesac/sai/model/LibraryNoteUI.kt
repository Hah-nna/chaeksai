package com.jeong.sesac.sai.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class LikeUI(
    val id: String,
    val noteId: String,
    val userId: String,
    val isLiked: Boolean
) : Parcelable

@Parcelize
data class LibraryNoteUI(
    val id: String,
    val userInfo: UserInfo,
    val image: String,
    val title: String,
    val createdAt: Long,
    val libraryName: String,
    val content: String,
    val likes: Int,
) : Parcelable

@Parcelize
data class UserInfo(
    val id: String,
    val profile: String,
    val nickName: String
): Parcelable

@Parcelize
data class UserUI(
    val id: String,
    val profile: String,
    val nickname: String,
    val createdAt: Long,
    val providerInfo: ProviderTypeUI = ProviderTypeUI.KAKAO
) : Parcelable

@Parcelize
data class CommentUI(
    val id: String,
    val userInfo: UserInfo,
    val content: String,
    val createdAt: Long
) : Parcelable

@Parcelize
enum class ProviderTypeUI : Parcelable {
    KAKAO,
    NAVER,
    GOOGLE
}

@Parcelize
data class PlaceInfoUI(
    val id: String,
    val address: String,
    val phone: String,
    val place: String,
    val placeUrl: String,
    val x: String,
    val y: String
) : Parcelable
