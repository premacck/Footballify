@file:JvmName("LineupsConverter")

package life.plank.juna.zone.data.local.typeconverter

import android.arch.persistence.room.TypeConverter
import com.google.gson.reflect.TypeToken
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.Formation
import life.plank.juna.zone.data.model.FormationList

class LineupsConverter {

    @TypeConverter
    fun formationsListFromString(formationsString: String?): List<Formation>? {
        return formationsString?.let { ZoneApplication.getGson().fromJson(it, object : TypeToken<List<List<Formation>>>() {}.type) }
    }

    @TypeConverter
    fun formationsListToString(formations: List<Formation>?): String? {
        return formations?.let { ZoneApplication.getGson().toJson(it) }
    }

    @TypeConverter
    fun formationListFromString(formationListString: String?): List<FormationList>? {
        return formationListString?.let { ZoneApplication.getGson().fromJson(it, object : TypeToken<List<List<FormationList>>>() {}.type) }
    }

    @TypeConverter
    fun formationListToString(formationList: List<FormationList>?): String? {
        return formationList?.let { ZoneApplication.getGson().toJson(it) }
    }
}