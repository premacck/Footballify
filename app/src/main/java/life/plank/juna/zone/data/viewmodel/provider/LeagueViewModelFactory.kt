package life.plank.juna.zone.data.viewmodel.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import life.plank.juna.zone.data.viewmodel.LeagueViewModel

class LeagueViewModelFactory(private val leagueId: Long) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LeagueViewModel() as T
    }
}
