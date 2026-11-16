package com.example.banqueteriapaolicias.data.session

import android.content.Context

class SessionManager(context: Context) {

    private val prefs =
        context.applicationContext.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    fun saveSession(correo: String, rol: String) {
        prefs.edit()
            .putString(KEY_CORREO, correo)
            .putString(KEY_ROL, rol)
            .apply()
    }

    fun getRol(): String? = prefs.getString(KEY_ROL, null)

    fun getCorreo(): String? = prefs.getString(KEY_CORREO, null)

    fun isLoggedIn(): Boolean = getRol() != null

    fun clear() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val KEY_CORREO = "correo"
        const val KEY_ROL = "rol"
    }
}