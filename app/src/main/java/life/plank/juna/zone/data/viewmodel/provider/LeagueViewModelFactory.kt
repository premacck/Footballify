package life.plank.juna.zone.data.viewmodel.provider

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import life.plank.juna.zone.data.viewmodel.LeagueViewModel

class LeagueViewModelFactory(private val leagueId: Long) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LeagueViewModel(leagueId) as T
    }
}
