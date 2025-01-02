package com.jeong.sesac.datamodule.datasource

import com.jeong.sesac.domain.model.UserInfo

interface FireBaseDataSource {
      // 로그인
      suspend fun createUser(userInfo: UserInfo) : Boolean
      suspend fun getDuplicateNickname(nickname : String) : Boolean
      // 유저정보
//      fun getUserInfo()
//      fun updateProfile()
//      fun updateNickName()

      // 홈에서 보일 금주 등록된 쪽지, 찾기완료된 쪽지 => noteUserInteraction에서 가져와서 레포에서 필터링해서 뷰모델로 보냄
//      fun getHomeNoteList()

      // 레코드탭에서 보여줄 것들 => noteUserInteraction에서 가져와서 레포에서 필터링해서 뷰모델로 보냄
      // noteUserInteraction / noteUserInteraction / noteUserInteraction
      // fun get 등록한쪽지, 찾은쪽지, 좋아요한 쪽지()
//      fun getRecordNotes()

      // 쪽지등록
//      fun postNote()
//      fun getNoteInfo()
//      fun deleteNote()


      // 쪽지찾기
//      fun getSearchNoteList()
//      fun getSearchNoteInfo()
//      fun postSearchNote()

      // 좋아요 업뎃
//      fun updateLikes()

      // 팔로우
//      fun updateFollow()
//      fun updateFollowing()

      // 리뷰
//      fun postReview()
//      fun getReviews()
}