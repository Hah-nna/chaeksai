package com.jeong.sesac.datamodule.datasource

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow


object PreferenceDataStoreConstants {
 val PROMOTION_ALERT = booleanPreferencesKey("promotion_alert")
 val FOLLOW_ALERT = booleanPreferencesKey("follow_alert")
 val LIKED_ALERT = booleanPreferencesKey("liked_alert")
 val FOUND_NOTE_ALERT = booleanPreferencesKey("found_note_alert")
 val USER_NICKNAME = stringPreferencesKey("")
}

// myPage, 닉네임 체크에 사용할 DataStore
interface DataStoreDataSource {
     suspend fun getUserName(key : Preferences.Key<String>, defaultValue: String): Flow<String>
     suspend fun setUserName(key: Preferences.Key<String>, value : String)
     suspend fun clearDataStore()

}