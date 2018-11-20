package life.plank.juna.zone.util.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_time_to_next_match.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.DataUtil.getSpecifiedLeague
import life.plank.juna.zone.util.DateUtil.getTimeToNextMatch
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import org.jetbrains.anko.runOnUiThread
import java.net.HttpURLConnection.HTTP_OK

class NextMatchLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        View.inflate(context, R.layout.item_time_to_next_match, this)
    }

    fun showNextMatchOnly(restApi: RestApi) {
        time_to_next_match_shimmer.startShimmerAnimation()
        restApi.getNextEvent(findString(R.string.football), getToken())
                .doOnTerminate {
                    context?.runOnUiThread {
                        time_to_next_match_shimmer.stopShimmerAnimation()
                        match_status.background = null
                        match_between.background = null
                    }
                }
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("getNextEvent()", "ERROR: ", it)
                }, {
                    when (it.code()) {
                        HTTP_OK -> {
                            val nextMatch = it.body()
                            nextMatch?.run {
                                getSpecifiedLeague(leagueName)?.run { league_logo.setImageResource(leagueLogo) }

                                Glide.with(context).load(homeTeamLogo).into(home_team_logo)
                                Glide.with(context).load(awayTeamLogo).into(visiting_team_logo)

                                match_status.text = getTimeToNextMatch(matchStartTime)
                                match_between.text = displayname
                            }
                        }
                        else -> {
                            match_between.setText(R.string.next_match_data_not_available)
                            errorToast(R.string.something_went_wrong, it)
                        }
                    }
                })
    }
}