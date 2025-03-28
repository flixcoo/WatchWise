package com.github.nullsafe.watchwise.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_ID = intPreferencesKey("user_id")
        val LANGUAGE = stringPreferencesKey("language")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val THEME = stringPreferencesKey("theme")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            UserPreferences(
                userName = preferences[PreferencesKeys.USER_NAME].orEmpty(),
                userId = preferences[PreferencesKeys.USER_ID] ?: 0,
                language = preferences[PreferencesKeys.LANGUAGE].takeUnless { it.isNullOrBlank() }
                    ?: Locale.getDefault().toLanguageTag(),
                isDarkMode = preferences[PreferencesKeys.IS_DARK_MODE] ?: false,
                theme = preferences[PreferencesKeys.THEME].orEmpty()
            )
        }

    suspend fun updateUserName(userName: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = userName
        }
    }

    suspend fun clearUserName() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_NAME)
        }
    }

    suspend fun updateLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language
        }
    }

    suspend fun updateDarkMode(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] = isDarkMode
        }
    }

    suspend fun updateTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme
        }
    }
}