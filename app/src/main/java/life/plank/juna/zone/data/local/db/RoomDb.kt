package life.plank.juna.zone.data.local.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.local.typeconverter.DateConverter
import life.plank.juna.zone.data.local.typeconverter.LineupsConverter
import life.plank.juna.zone.data.local.typeconverter.MatchConverter
import life.plank.juna.zone.data.model.FootballTeam

@Database(entities = [FootballTeam::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, LineupsConverter::class, MatchConverter::class)
abstract class RoomDb : RoomDatabase() {

    companion object {
        private const val ROOM_DB_NAME = "junaDatabase.db"
        private var INSTANCE: RoomDb? = null

        //        synchronized block and a second null-check for thread safety
        fun getInstance(): RoomDb? {
            if (INSTANCE == null) {
                synchronized(RoomDb::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(ZoneApplication.getContext(), RoomDb::class.java, ROOM_DB_NAME).build()
                    }
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}