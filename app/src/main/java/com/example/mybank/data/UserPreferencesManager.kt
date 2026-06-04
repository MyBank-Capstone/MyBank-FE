package com.example.mybank.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("mybank_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_AI_PERSONALIZATION = "is_ai_personalization_enabled"
        private const val KEY_HAS_ANSWERED_CONSENT = "has_answered_ai_consent"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_ACCESS_TOKEN = "access_token"
    }

    // Menyimpan dan mengambil ID User
    var userId: Int
        get() = prefs.getInt(KEY_USER_ID, -1)
        set(value) = prefs.edit().putInt(KEY_USER_ID, value).apply()

    // Menyimpan access token
    var accessToken: String?
        get() = prefs.getString(KEY_ACCESS_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_ACCESS_TOKEN, value).apply()

    // Menyimpan dan mengambil Nama User
    var userName: String?
        get() = prefs.getString(KEY_USER_NAME, null)
        set(value) = prefs.edit().putString(KEY_USER_NAME, value).apply()

    // Menyimpan dan mengambil status ON/OFF AI
    var isAiPersonalizationEnabled: Boolean
        get() = prefs.getBoolean(KEY_AI_PERSONALIZATION, false) // default false
        set(value) = prefs.edit().putBoolean(KEY_AI_PERSONALIZATION, value).apply()

    // Mencatat apakah user sudah pernah menjawab pop-up di awal
    var hasAnsweredAiConsent: Boolean
        get() = prefs.getBoolean(KEY_HAS_ANSWERED_CONSENT, false)
        set(value) = prefs.edit().putBoolean(KEY_HAS_ANSWERED_CONSENT, value).apply()

    // Menghapus data sesi
    fun clearSession() {
        prefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_USER_NAME)
            .remove(KEY_USER_ID)
            .apply()
    }
}