package life.plank.juna.zone.view.fragment.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.bumptech.glide.RequestManager
import com.google.gson.Gson
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.viewmodel.LeagueViewModel
import life.plank.juna.zone.util.facilis.BaseCard

abstract class BaseLeagueFragment : BaseCard() {

    protected lateinit var leagueViewModel: LeagueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leagueViewModel = ViewModelProviders.of(this).get(LeagueViewModel::class.java)
    }

    abstract fun getGlide(): RequestManager

    abstract fun getTheGson(): Gson

    abstract fun getTheLeague(): League
}