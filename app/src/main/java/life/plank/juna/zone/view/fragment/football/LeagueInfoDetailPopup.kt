package life.plank.juna.zone.view.fragment.football

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_league_info_detail.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.AppConstants
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.view.adapter.league.PlayerStatsAdapter
import life.plank.juna.zone.view.adapter.league.StandingTableAdapter
import life.plank.juna.zone.view.adapter.league.TeamStatsAdapter
import life.plank.juna.zone.view.fragment.base.BaseBlurPopup
import javax.inject.Inject

class LeagueInfoDetailPopup : BaseBlurPopup() {

    @Inject
    lateinit var picasso: Picasso

    private lateinit var viewToLoad: String
    private var standingTableAdapter: StandingTableAdapter? = null
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
            AppConstants.STANDINGS -> {
                standingTableAdapter = StandingTableAdapter(Glide.with(this))
                standing_recycler_view.adapter = standingTableAdapter!!
                standingTableAdapter?.update(arguments?.getParcelableArrayList(getString(R.string.intent_list)))
                toggleStatsHeaderVisibility(LinearLayout.VISIBLE, LinearLayout.GONE, LinearLayout.GONE)
            }
            AppConstants.TEAM_STATS -> {
                teamStatsAdapter = TeamStatsAdapter(Glide.with(this))
                standing_recycler_view.adapter = teamStatsAdapter!!
                teamStatsAdapter?.update(arguments?.getParcelableArrayList(getString(R.string.intent_list)))
                toggleStatsHeaderVisibility(LinearLayout.GONE, LinearLayout.VISIBLE, LinearLayout.GONE)
            }
            AppConstants.PLAYER_STATS -> {
                playerStatsAdapter = PlayerStatsAdapter()
                standing_recycler_view.adapter = playerStatsAdapter!!
                playerStatsAdapter?.update(arguments?.getParcelableArrayList(getString(R.string.intent_list)))
                toggleStatsHeaderVisibility(LinearLayout.GONE, LinearLayout.GONE, LinearLayout.VISIBLE)
            }
        }
    }

    private fun toggleStatsHeaderVisibility(standingsHeaderVisibility: Int, teamStatsHeaderVisibility: Int, playerStatsHeaderVisibility: Int) {
        standing_header_layout.visibility = standingsHeaderVisibility
        team_stats_header_layout.visibility = teamStatsHeaderVisibility
        player_stats_header.visibility = playerStatsHeaderVisibility
    }

    override fun getBlurLayout(): BlurLayout? = root_blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = root_blur_layout

    override fun onDestroy() {
        standingTableAdapter = null
        teamStatsAdapter = null
        playerStatsAdapter = null
        super.onDestroy()
    }
}
