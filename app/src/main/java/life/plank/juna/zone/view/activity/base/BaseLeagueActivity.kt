package life.plank.juna.zone.view.activity.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle

import com.google.gson.Gson
import com.squareup.picasso.Picasso

import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.data.viewmodel.LeagueViewModel

abstract class BaseLeagueActivity : BaseCardActivity() {

    protected lateinit var leagueViewModel: LeagueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leagueViewModel = ViewModelProviders.of(this).get(LeagueViewModel::class.java)
    }

    abstract val picasso: Picasso

    abstract val gson: Gson

    abstract val league: League
}