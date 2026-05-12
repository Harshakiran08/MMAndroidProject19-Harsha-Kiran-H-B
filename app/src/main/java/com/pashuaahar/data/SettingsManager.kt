package com.pashuaahar.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {

    companion object {
        val THEME_MODE = booleanPreferencesKey("theme_mode")
        val KANNADA_LANG = booleanPreferencesKey("kannada_lang")
        val LANGUAGE_CODE = stringPreferencesKey("language_code")
        val PRICE_MAIZE = floatPreferencesKey("price_maize")
        val PRICE_COTTONSEED = floatPreferencesKey("price_cottonseed")
        val PRICE_BRAN = floatPreferencesKey("price_bran")
        val PRICE_MARKET = floatPreferencesKey("price_market")
        val LOGGED_IN_USER_ID = intPreferencesKey("logged_in_user_id")
        val LOCATION = stringPreferencesKey("market_location")
    }

    val isDarkModeFlow: Flow<Boolean> = context.dataStore.data.map { it[THEME_MODE] ?: false }
    val isKannadaFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        val code = prefs[LANGUAGE_CODE]
        when {
            code != null -> code == "kn"
            else -> prefs[KANNADA_LANG] ?: false
        }
    }
    val languageCodeFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[LANGUAGE_CODE] ?: if (prefs[KANNADA_LANG] == true) "kn" else "en"
    }
    val loggedInUserIdFlow: Flow<Int> = context.dataStore.data.map { it[LOGGED_IN_USER_ID] ?: -1 }
    val locationFlow: Flow<String> = context.dataStore.data.map { it[LOCATION] ?: "Mandya Market" }

    val maizePriceFlow: Flow<Float> = context.dataStore.data.map { it[PRICE_MAIZE] ?: 25.0f }
    val cottonseedPriceFlow: Flow<Float> = context.dataStore.data.map { it[PRICE_COTTONSEED] ?: 40.0f }
    val branPriceFlow: Flow<Float> = context.dataStore.data.map { it[PRICE_BRAN] ?: 20.0f }
    val marketPriceFlow: Flow<Float> = context.dataStore.data.map { it[PRICE_MARKET] ?: 35.0f }

    suspend fun saveThemeMode(isDark: Boolean) {
        context.dataStore.edit { it[THEME_MODE] = isDark }
    }

    suspend fun saveLanguage(isKannada: Boolean) {
        context.dataStore.edit {
            it[KANNADA_LANG] = isKannada
            it[LANGUAGE_CODE] = if (isKannada) "kn" else "en"
        }
    }

    suspend fun saveLanguageCode(code: String) {
        context.dataStore.edit {
            it[LANGUAGE_CODE] = code
            it[KANNADA_LANG] = code == "kn"
        }
    }

    suspend fun saveLoggedInUserId(userId: Int) {
        context.dataStore.edit { it[LOGGED_IN_USER_ID] = userId }
    }

    suspend fun saveLocation(location: String) {
        context.dataStore.edit { it[LOCATION] = location }
    }

    suspend fun savePrices(maize: Float, cottonseed: Float, bran: Float, market: Float) {
        context.dataStore.edit {
            it[PRICE_MAIZE] = maize
            it[PRICE_COTTONSEED] = cottonseed
            it[PRICE_BRAN] = bran
            it[PRICE_MARKET] = market
        }
    }
}
