package com.echoeyecodes.sinnerman.Utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PreferenceManager(context: Context) {
    private val dataStore = context.createDataStore(name = "preference_manager")

    companion object{
        val THEME_TYPE = preferencesKey<Boolean>("theme")
    }

    suspend fun setPreferences(value:Boolean){
        dataStore.edit { preferences -> preferences[THEME_TYPE] = value }
    }

    val theme = dataStore.data.catch { emit(emptyPreferences()) }.map { preference -> preference[THEME_TYPE] }

}