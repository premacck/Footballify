package life.plank.juna.zone.data.local.repository

import android.support.annotation.WorkerThread
import life.plank.juna.zone.data.local.dao.MatchDetailsDao
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.viewmodel.MatchDetailViewModel
import org.jetbrains.anko.doAsync

/**
 * Repository class for interacting with the [MatchDetailsDao] and updating the [MatchDetailViewModel]
 * This class also handles inserting, updating and deleting elements in the [MatchDetails] table
 */
class MatchDetailRepository : BaseRepository() {

    private val matchDetailsDao: MatchDetailsDao = roomDb.matchDetailsDao()

    @WorkerThread
    fun getMatchDetails(matchId: Long): MatchDetails = matchDetailsDao.getMatchDetails(matchId)

    fun insertMatchDetails(matchDetails: MatchDetails) {
        doAsync { matchDetailsDao.insertMatchDetail(matchDetails) }
    }

    fun deleteMatchDetails(matchDetails: MatchDetails) {
        doAsync { matchDetailsDao.deleteMatchDetails(matchDetails) }
    }
}