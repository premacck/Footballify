package life.plank.juna.zone.sharedpreference

import android.content.SharedPreferences
import com.google.gson.reflect.TypeToken
import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.user.*
import life.plank.juna.zone.injection.module.NetworkModule.GSON
import life.plank.juna.zone.service.CommonDataService.findString

object CurrentUser {

    private val userPrefs: SharedPreferences
        get() = getSharedPrefs(findString(R.string.pref_user_details))

    val userEmail: String?
        get() = userPrefs.getString(findString(R.string.pref_email_address), null)

    val userId: String?
        get() = userPrefs.getString(findString(R.string.pref_object_id), null)

    val displayName: String?
        get() = userPrefs.getString(findString(R.string.pref_display_name), null)

    val handle: String
        get() = userPrefs.getString(findString(R.string.pref_handle), null) ?: ""

    val profilePicUrl: String?
        get() = userPrefs.getString(findString(R.string.pref_profile_pic_url), null)

    val location: String?
        get() = userPrefs.getString(findString(R.string.pref_location), null)

    val country: String?
        get() = userPrefs.getString(findString(R.string.pref_country), null)

    val city: String?
        get() = userPrefs.getString(findString(R.string.pref_city), null)

    val userPreferences: List<UserPreference>?
        get() {
            val userPrefString = userPrefs.getString(findString(R.string.pref_user_preferences), null)
            if (!isNullOrEmpty(userPrefString)) {
                val userPreferences = GSON.fromJson<List<UserPreference>>(userPrefString, object : TypeToken<List<UserPreference>>() {

                }.type)
                return if (isNullOrEmpty(userPreferences)) null else userPreferences
            }
            return null
        }

    val dob: String
        get() = userPrefs.getString(findString(R.string.pref_dob), null) ?: ""

    fun saveUser(user: User) {
        userPrefs.edit()
                .putString(findString(R.string.pref_object_id), user.objectId)
                .putString(findString(R.string.pref_display_name), user.displayName)
                .putString(findString(R.string.pref_handle), user.handle)
                .putString(findString(R.string.pref_email_address), user.emailAddress)
                .putString(findString(R.string.pref_profile_pic_url), user.profilePictureUrl)
                .putString(findString(R.string.pref_country), user.country)
                .putString(findString(R.string.pref_city), user.city)
                .putString(findString(R.string.pref_user_preferences), GSON.toJson(user.userPreferences))
                .putString(findString(R.string.pref_dob), user.dateOfBirth)
                .apply()
    }

    fun saveUserEmail(email: String) {
        userPrefs.edit().putString(findString(R.string.pref_email_address), email).apply()
    }

    fun saveUserLoginStatus(isLoggedIn: Boolean) {
        userPrefs.edit().putBoolean(findString(R.string.pref_is_logged_in), isLoggedIn).apply()
    }

    fun saveProfilePicUrl(url: String?) {
        userPrefs.edit().putString(findString(R.string.pref_profile_pic_url), url).apply()
    }

    fun saveLocation(location: String?) {
        userPrefs.edit().putString(findString(R.string.pref_location), location).apply()
    }
}
