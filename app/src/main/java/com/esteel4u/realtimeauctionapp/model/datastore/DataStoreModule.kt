package com.esteel4u.realtimeauctionapp.model.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.esteel4u.realtimeauctionapp.model.data.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore  by preferencesDataStore(name = "dataStore")

class DataStoreModule (private val context : Context){

    companion object{
        val uid = stringPreferencesKey("uid")
        val userId = stringPreferencesKey("userId")
        val gcsCompCode = stringPreferencesKey("gcsCompCode")
        val userName = stringPreferencesKey("userName")
        val setAlarm = stringPreferencesKey("setAlarm")
    }
    // stringKey 키 값과 대응되는 값 반환
    val user : Flow<UserData> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            UserData(
                preferences[uid] ?:"",
                preferences[userId] ?:"",
                preferences[gcsCompCode] ?:"",
                preferences[userName] ?:"",
                preferences[setAlarm] ?: ""
            )


        }

    // String값을 stringKey 키 값에 저장
    suspend fun setUserData(user : UserData){
        context.dataStore.edit { preferences ->
            preferences[uid] = user.uid!!
            preferences[userId] = user.userId!!
            preferences[gcsCompCode] = user.gcsCompCode!!
            preferences[userName] = user.userName!!
            preferences[setAlarm] = user.setAlarm!!
        }
    }

    // String값을 stringKey 키 값에 저장
    suspend fun updateAlarmUser(alarm : String){
        context.dataStore.edit { preferences ->

            preferences[setAlarm] =alarm
        }
    }

    suspend fun clearData(){
        context.dataStore.edit {
            it.clear()
        }
    }

}