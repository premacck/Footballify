package life.plank.juna.zone.util.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.next_match_row.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.DataUtil.getSpecifiedLeague
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.common.onTerminate
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.time.DateUtil.getTimeToNextMatch
import java.net.HttpURLConnection.HTTP_OK

class NextMatchLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        View.inflate(context, R.layout.next_match_row, this)
    }

    fun showNextMatchOnly(restApi: RestApi) {
        time_to_next_match_shimmer.startShimmerAnimation()
        restApi.getNextMatches(PreferenceManager.CurrentUser.getUserPreferences()[0].zonePreferences?.leagues, getToken())
                .onTerminate {
                    time_to_next_match_shimmer.stopShimmerAnimation()
                    match_status.background = null
                    match_between.background = null
                }
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("getNextEvent()", "ERROR: ", it)
                }, {
                    when (it.code()) {
                        HTTP_OK -> {
                            it.body()?.run {
                                if (!isEmpty()) {
                                    val nextMatch = it.body()!![0]
                                    nextMatch?.run {
                                        getSpecifiedLeague(leagueName)?.run { league_logo.setImageResource(leagueLogo) }

                                        Glide.with(context).load(homeTeamLogo).into(home_team_logo)
                                        Glide.with(context).load(awayTeamLogo).into(visiting_team_logo)

                                        match_status.text = getTimeToNextMatch(matchStartTime)
                                        match_between.text = displayName
                                    }
                                }
                            }
                        }
                        else -> {
                            match_between.setText(R.string.next_match_data_not_available)
                            home_team_logo.visibility = View.INVISIBLE
                            visiting_team_logo.visibility = View.INVISIBLE
                            league_logo.visibility = View.INVISIBLE
                            errorToast(R.string.something_went_wrong, it)
                        }
                    }
                }, this)
    }
}