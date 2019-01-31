package life.plank.juna.zone.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import life.plank.juna.zone.ZoneApplication.Companion.appContext

fun getSharedPrefs(prefName: String): SharedPreferences = appContext.getSharedPreferences(prefName, Context.MODE_PRIVATE)
