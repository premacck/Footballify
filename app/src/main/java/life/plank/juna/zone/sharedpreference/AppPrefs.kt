package life.plank.juna.zone.sharedpreference

import android.content.SharedPreferences

import life.plank.juna.zone.R

import life.plank.juna.zone.service.CommonDataService.findString

object AppPrefs {

    private val appPrefs: SharedPreferences
        get() = getSharedPrefs(findString(R.string.pref_app_preferences))

    val isEnterToSend: Boolean
        get() = appPrefs.getBoolean(findString(R.string.pref_enter_to_send), true)

    fun saveEnterToSend(isEnterToSend: Boolean) {
        appPrefs.edit().putBoolean(findString(R.string.pref_enter_to_send), isEnterToSend).apply()
    }
}
