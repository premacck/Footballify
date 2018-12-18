package life.plank.juna.zone.view.latestMatch

import android.app.Activity
import android.view.*
import com.ahamed.multiviewadapter.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.next_match_row.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.NextMatch
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.DataUtil.getSpecifiedLeague
import life.plank.juna.zone.util.common.launchMatchBoard
import life.plank.juna.zone.util.facilis.onElevatingClick
import life.plank.juna.zone.util.time.DateUtil
import life.plank.juna.zone.view.activity.base.BaseCardActivity

class NextMatchBinder(private val activity: Activity, private val restApi: RestApi) : ItemBinder<NextMatch, NextMatchBinder.NextMatchViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): NextMatchViewHolder =
            NextMatchViewHolder(inflater.inflate(R.layout.next_match_row, parent, false))

    override fun bind(holder: NextMatchViewHolder, item: NextMatch) {
        holder.itemView.match_status.background = null
        holder.itemView.match_between.background = null
        holder.itemView.match_status.text = DateUtil.getTimeToNextMatch(item.matchStartTime)
        holder.itemView.match_between.text = item.displayName

        getSpecifiedLeague(item.leagueName)?.run { holder.itemView.league_logo.setImageResource(leagueLogo) }
        Glide.with(activity).load(item.homeTeamLogo).into(holder.itemView.home_team_logo)
        Glide.with(activity).load(item.awayTeamLogo).into(holder.itemView.visiting_team_logo)

        holder.itemView.onElevatingClick(0) {
            (activity as? BaseCardActivity)?.launchMatchBoard(restApi, item.matchId)
        }
    }

    override fun canBindData(item: Any): Boolean = item is NextMatch

    class NextMatchViewHolder(itemView: View) : ItemViewHolder<NextMatch>(itemView)
}