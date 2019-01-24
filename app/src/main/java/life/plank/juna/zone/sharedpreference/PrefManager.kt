package life.plank.juna.zone.sharedpreference

import android.content.*
import life.plank.juna.zone.ZoneApplication.getContext

fun getSharedPrefs(prefName: String): SharedPreferences = getContext().getSharedPreferences(prefName, Context.MODE_PRIVATE)
