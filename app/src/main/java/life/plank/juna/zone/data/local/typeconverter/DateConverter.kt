package life.plank.juna.zone.data.local.typeconverter

import androidx.room.TypeConverter
import java.util.*


/**
 * Type converter for [Date] properties
 */
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