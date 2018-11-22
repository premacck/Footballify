package life.plank.juna.zone.data.viewmodel.provider

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import life.plank.juna.zone.data.viewmodel.MatchDetailViewModel

class MatchDetailsViewModelFactory(private val matchId: Long) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MatchDetailViewModel() as T
    }
}