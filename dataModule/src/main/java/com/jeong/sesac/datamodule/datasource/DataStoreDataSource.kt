package com.jeong.sesac.datamodule.datasource

// myPage에 사용할 DataStore
interface DataStoreDataSource {
      fun getAppSettings()
      fun updatePromotionAlert()
      fun updateFollowAlert()
      fun updateLikedNoteAlert()
      fun updateFoundedNoteAlert()

}