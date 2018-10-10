package life.plank.juna.zone.data.local.typeconverter

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun longToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToLong(value: Date?): Long? {
        return value?.time
    }
}