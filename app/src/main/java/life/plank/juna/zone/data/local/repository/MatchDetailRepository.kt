package life.plank.juna.zone.data.local.repository

import life.plank.juna.zone.data.local.dao.MatchDetailsDao
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.data.viewmodel.MatchDetailViewModel

/**
 * Repository class for interacting with the [MatchDetailsDao] and updating the [MatchDetailViewModel]
 * This class also handles inserting, updating and deleting elements in the [MatchDetails] table
 */
class MatchDetailRepository(matchId: Long) : BaseRepository()