package life.plank.juna.zone.data.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.RoomDatabase

/**
 * [ViewModel] class for getting data from the [RoomDatabase] and API calls, and updating the [LiveData] that the UI will be observing.
 */
class MatchDetailViewModel(matchId: Long) : ViewModel()