package life.plank.juna.zone.view.fragment.football

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_league_info_detail.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.PlayerStats
import life.plank.juna.zone.data.model.TeamStats
import life.plank.juna.zone.util.common.AppConstants
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.view.adapter.league.PlayerStatsAdapter
import life.plank.juna.zone.view.adapter.league.TeamStatsAdapter
import life.plank.juna.zone.view.fragment.base.BaseBlurPopup

class LeagueInfoDetailPopup : BaseBlurPopup() {

    private lateinit var viewToLoad: String
    private var teamStatsAdapter: TeamStatsAdapter? = null
    private var playerStatsAdapter: PlayerStatsAdapter? = null

    companion object {
        val TAG: String = LeagueInfoDetailPopup::class.java.simpleName
        fun newInstance(whatToLoad: String, list: ArrayList<out Parcelable>) = LeagueInfoDetailPopup().apply {
            arguments = Bundle().apply {
                putString(findString(R.string.intent_load_view), whatToLoad)
                putParcelableArrayList(findString(R.string.intent_list), list)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
        viewToLoad = arguments?.getString(getString(R.string.intent_load_view))!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.popup_league_info_detail, container, false)

    override fun doOnStart() {
        header.text = viewToLoad
        when (viewToLoad) {
            AppConstants.TEAM_STATS -> {
                teamStatsAdapter = TeamStatsAdapter(Glide.with(this))
                standing_recycler_view.adapter = teamStatsAdapter!!
                teamStatsAdapter?.update(arguments?.getParcelableArrayList<TeamStats>(getString(R.string.intent_list)) as MutableList<TeamStats>)
            }
            AppConstants.PLAYER_STATS -> {
                playerStatsAdapter = PlayerStatsAdapter()
                standing_recycler_view.adapter = playerStatsAdapter!!
                playerStatsAdapter?.update(arguments?.getParcelableArrayList<PlayerStats>(getString(R.string.intent_list)) as MutableList<PlayerStats>)
            }
        }
        addHeader(viewToLoad)
    }

    private fun addHeader(viewToLoad: String) {
        when (viewToLoad) {
            AppConstants.TEAM_STATS -> headers_container.addView(View.inflate(context, R.layout.team_stats_header, null))
            AppConstants.PLAYER_STATS -> headers_container.addView(View.inflate(context, R.layout.player_stats_header, null))
        }
    }

    override fun getBlurLayout(): BlurLayout? = root_blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = root_blur_layout

    override fun onDestroy() {
        teamStatsAdapter = null
        playerStatsAdapter = null
        super.onDestroy()
    }
}
