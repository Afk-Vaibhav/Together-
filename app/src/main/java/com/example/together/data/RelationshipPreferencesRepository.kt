package com.example.together.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "relationship_prefs")

class RelationshipPreferencesRepository(private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val START_TIMESTAMP = longPreferencesKey("start_timestamp")
        val SETUP_COMPLETED = booleanPreferencesKey("setup_completed")
        val MONTHLY_NOTIFICATIONS = booleanPreferencesKey("monthly_notifications")
        val YEARLY_NOTIFICATIONS = booleanPreferencesKey("yearly_notifications")
    }

    val relationshipStart: Flow<LocalDateTime?> = dataStore.data.map { prefs ->
        prefs[START_TIMESTAMP]?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
        }
    }

    val isSetupCompleted: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[SETUP_COMPLETED] ?: false
    }

    val monthlyNotificationsEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[MONTHLY_NOTIFICATIONS] ?: true
    }

    val yearlyNotificationsEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[YEARLY_NOTIFICATIONS] ?: true
    }

    suspend fun saveRelationshipStart(start: LocalDateTime) {
        dataStore.edit { prefs ->
            prefs[START_TIMESTAMP] = start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            prefs[SETUP_COMPLETED] = true
        }
    }

    suspend fun setMonthlyNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[MONTHLY_NOTIFICATIONS] = enabled
        }
    }

    suspend fun setYearlyNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[YEARLY_NOTIFICATIONS] = enabled
        }
    }
}
