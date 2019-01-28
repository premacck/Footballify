package life.plank.juna.zone.data.local.db

import androidx.room.*
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.local.dao.*
import life.plank.juna.zone.data.model.football.LeagueInfo
import life.plank.juna.zone.data.local.typeconverter.*
import life.plank.juna.zone.data.model.football.MatchDetails

@Database(version = 1, exportSchema = false, entities = [
    MatchDetails::class,
    LeagueInfo::class
])
@TypeConverters(
        DateConverter::class,
        LineupsConverter::class,
        MatchConverter::class
)
abstract class RoomDb : RoomDatabase() {

    abstract fun matchDetailsDao(): MatchDetailsDao
    abstract fun leagueDao(): LeagueDao

    companion object {
        private const val ROOM_DB_NAME = "junaDatabase.db"
        private var INSTANCE: RoomDb? = null

        //        synchronized block and a second null-check for thread safety
        fun getInstance(): RoomDb {
            if (INSTANCE == null) {
                synchronized(RoomDb::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(ZoneApplication.getContext(), RoomDb::class.java, ROOM_DB_NAME).build()
                    }
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}