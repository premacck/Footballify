package life.plank.juna.zone.sharedpreference

import android.util.Log
import androidx.annotation.StringRes
import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.R.string.*
import life.plank.juna.zone.service.CommonDataService.findString
import net.openid.appauth.AuthState
import org.json.JSONException

private fun getAuthPrefs() = getSharedPrefs(findString(pref_login_credentails))

val IdToken: String?
    get() {
        val tokenValue = getAuthPrefs().getString(findString(pref_azure_token), null)
        return if (isNullOrEmpty(tokenValue)) null else "${findString(bearer)} $tokenValue"
    }

fun saveTokens(idToken: String, refreshToken: String) {
    getAuthPrefs().edit()
            .putString(findString(pref_azure_token), idToken)
            .putString(findString(pref_refresh_token), refreshToken)
            .apply()
}

fun saveTokensValidity(additionalParameters: Map<String, String>) {
    try {
        val notBefore = additionalParameters[findString(pref_not_before)]?.toLong()!!
        val idTokenExpiresIn = additionalParameters[findString(pref_id_token_expires_in)]?.toLong()!!
        val refreshTokenExpiresIn = additionalParameters[findString(pref_refresh_token_expires_in)]?.toLong()!!
        val idTokenValidity = notBefore + idTokenExpiresIn
        val refreshTokenValidity = notBefore + refreshTokenExpiresIn
        getAuthPrefs().edit()
                .putLong(findString(pref_id_token_validity), idTokenValidity)
                .putLong(findString(pref_refresh_token_validity), refreshTokenValidity)
                .apply()
    } catch (e: Exception) {
        Log.e("saveTokensValidity()", e.message, e)
    }
}

fun checkTokenValidity(@StringRes whichToken: Int): Boolean = getAuthPrefs().getLong(findString(whichToken), 0) > System.currentTimeMillis() / 1000

fun saveAuthState(authState: AuthState) = getAuthPrefs().edit().putString(findString(pref_auth_state), authState.jsonSerializeString()).apply()

fun getSavedAuthState(): AuthState? = try {
    val serializedAuthState = getSharedPrefs(findString(pref_login_credentails)).getString(findString(pref_auth_state), null)
    if (serializedAuthState != null) AuthState.jsonDeserialize(serializedAuthState) else null
} catch (e: JSONException) {
    Log.e("saveAuthState()", e.message, e)
    null
}