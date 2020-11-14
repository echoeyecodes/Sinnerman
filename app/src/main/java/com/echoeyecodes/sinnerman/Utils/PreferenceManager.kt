package com.echoeyecodes.sinnerman.Utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class PreferenceManager(context: Context) {
    private val dataStore = context.createDataStore(name = "preference_manager")

    companion object{
        val CATEGORY_TYPE = preferencesKey<String>("category")
    }

    suspend fun setPreferences(value:String){
        dataStore.edit { preferences -> preferences[CATEGORY_TYPE] = value }
    }

    val category = dataStore.data.catch { emit(emptyPreferences()) }.map { preference -> preference[CATEGORY_TYPE] }

}