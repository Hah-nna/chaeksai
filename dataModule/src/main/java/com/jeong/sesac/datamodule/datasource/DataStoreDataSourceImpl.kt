package com.jeong.sesac.datamodule.datasource

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException

private val Context.jetpackDataStore by preferencesDataStore(name = "my_app_dataStore")

class DataStoreDataSourceImpl(context: Context) : DataStoreDataSource {
    private val dataSource = context.jetpackDataStore

    override suspend fun getUserName(
        key: Preferences.Key<String>,
        defaultValue: String
    ): Flow<String> {
        return dataSource.data.catch { exception ->
            if(exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            val result = preference[key] ?: defaultValue
            Log.d("dataStore getNickname", result)
            result
        }
    }

    override suspend fun setUserName(key: Preferences.Key<String>, value: String) {
        dataSource.edit { preference ->
            preference[key] = value
            Log.d("dataStore setUserName", preference[key].toString())
        }
    }

    override suspend fun clearDataStore() {
        dataSource.edit { it.clear() }
    }


}