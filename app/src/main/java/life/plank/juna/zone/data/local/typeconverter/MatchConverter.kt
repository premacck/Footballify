@file:JvmName("MatchConverter")

package life.plank.juna.zone.data.local.typeconverter

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.*

class MatchConverter {
    @TypeConverter
    fun highlightsListFromString(highlightsListString: String?): List<Highlights>? {
        return highlightsListString?.let { ZoneApplication.getGson().fromJson(it, object : TypeToken<List<Highlights>>() {}.type) }
    }

    @TypeConverter
    fun highlightsListToString(highlightsList: List<Highlights>?): String? {
        return highlightsList?.let { ZoneApplication.getGson().toJson(it) }
    }

    @TypeConverter
    fun matchEventListFromString(matchEventListString: String?): List<MatchEvent>? {
        return matchEventListString?.let { ZoneApplication.getGson().fromJson(it, object : TypeToken<List<MatchEvent>>() {}.type) }
    }

    @TypeConverter
    fun matchEventListToString(matchEventList: List<MatchEvent>?): String? {
        return matchEventList?.let { ZoneApplication.getGson().toJson(it) }
    }

    @TypeConverter
    fun commentaryListFromString(commentaryListString: String?): List<Commentary>? {
        return commentaryListString?.let { ZoneApplication.getGson().fromJson(it, object : TypeToken<List<Commentary>>() {}.type) }
    }

    @TypeConverter
    fun commentaryListToString(commentaryList: List<Commentary>?): String? {
        return commentaryList?.let { ZoneApplication.getGson().toJson(it) }
    }

    @TypeConverter
    fun standingsListFromString(standingsListString: String?): List<Standings>? {
        return standingsListString?.let { ZoneApplication.getGson().fromJson(it, object : TypeToken<List<Standings>>() {}.type) }
    }

    @TypeConverter
    fun standingsListToString(standingsList: List<Standings>?): String? {
        return standingsList?.let { ZoneApplication.getGson().toJson(it) }
    }

    @TypeConverter
    fun teamStatsListFromString(teamStatsListString: String?): List<TeamStats>? {
        return teamStatsListString?.let { ZoneApplication.getGson().fromJson(it, object : TypeToken<List<TeamStats>>() {}.type) }
    }

    @TypeConverter
    fun teamStatsListToString(teamStatsList: List<TeamStats>?): String? {
        return teamStatsList?.let { ZoneApplication.getGson().toJson(it) }
    }

    @TypeConverter
    fun scrubberDataListFromString(scrubberDataListString: String?): List<ScrubberData>? {
        return scrubberDataListString?.let { ZoneApplication.getGson().fromJson(it, object : TypeToken<List<ScrubberData>>() {}.type) }
    }

    @TypeConverter
    fun scrubberDataListToString(scrubberDataList: List<ScrubberData>?): String? {
        return scrubberDataList?.let { ZoneApplication.getGson().toJson(it) }
    }

    @TypeConverter
    fun fixtureByMatchDayListFromString(fixtureByMatchDatList: String?): List<FixtureByMatchDay>? {
        return fixtureByMatchDatList?.let { ZoneApplication.getGson().fromJson(it, object : TypeToken<List<FixtureByMatchDay>>() {}.type) }
    }

    @TypeConverter
    fun fixtureByMatchDayListToString(fixtureByMatchDatList: List<FixtureByMatchDay>?): String? {
        return fixtureByMatchDatList?.let { ZoneApplication.getGson().toJson(it) }
    }

    @TypeConverter
    fun playerStatsListStringFromString(playerStatsListString: String?): List<PlayerStats>? {
        return playerStatsListString?.let { ZoneApplication.getGson().fromJson(it, object : TypeToken<List<PlayerStats>>() {}.type) }
    }

    @TypeConverter
    fun playerStatsListStringToString(playerStatsListString: List<PlayerStats>?): String? {
        return playerStatsListString?.let { ZoneApplication.getGson().toJson(it) }
    }
}