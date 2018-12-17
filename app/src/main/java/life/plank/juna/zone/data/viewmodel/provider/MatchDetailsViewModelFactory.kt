package life.plank.juna.zone.data.viewmodel.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import life.plank.juna.zone.data.viewmodel.MatchDetailViewModel

class MatchDetailsViewModelFactory(private val matchId: Long) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MatchDetailViewModel() as T
    }
}