package com.jeong.sesac.domain.model


data class UserInfo(
    val userId: String,
    val nickName: String,
    val profile_img: String,
    val created_at: String,
    val hint_exchange_ticket: Int = 0,
    val hint_ticket: Int = 0,
    val provider_info: ProviderType = ProviderType.KAKAO
    ) {

fun toMap(): Map<String, Any> {
    return mapOf(
        "id" to userId,
        "nickname" to nickName,
        "profile" to profile_img,
        "created_at" to created_at,
        "hint_exchange_ticket" to hint_exchange_ticket,
        "hint_ticket" to hint_ticket,
        "provider_info" to provider_info.toString()
    )
}

}

enum class ProviderType {
    KAKAO,
    NAVER,
    GOOGLE
}