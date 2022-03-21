package id.myeco.myeco.core.source.local

import android.content.Context
import android.content.SharedPreferences
import com.auth0.android.jwt.JWT

class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences("myEco", Context.MODE_PRIVATE)

    companion object {
        const val ID_USER = "data"
    }

    fun saveIdUser(idUser: String) = prefs.edit().putString(ID_USER, idUser).apply()

    fun fetchAuthToken(): String? = prefs.getString(ID_USER, null)

    fun isTokenExp(): Boolean {
        return try {
            val jwt = JWT(prefs.getString(ID_USER, "") as String)
            jwt.isExpired(1)
        } catch (e: Exception) {
            true
        }
    }

    fun logout() = prefs.edit().clear().apply()
}