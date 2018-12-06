package life.plank.juna.zone.view.LatestMatch

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ahamed.multiviewadapter.BaseViewHolder
import com.ahamed.multiviewadapter.ItemBinder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.football_feed_row.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.NextMatch
import life.plank.juna.zone.util.time.DateUtil

class NextMatchBinder internal constructor(private val activity: Activity) :
        ItemBinder<NextMatch, NextMatchBinder.NextMatchViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): NextMatchViewHolder {

        return NextMatchViewHolder(inflater.inflate(R.layout.football_feed_row, parent, false))
    }

    override fun bind(holder: NextMatchViewHolder, item: NextMatch) {
        holder.itemView.match_status.text = DateUtil.getTimeToNextMatch(item.matchStartTime)
        holder.itemView.match_between.text = item.displayName

        Glide.with(activity).load(item.homeTeamLogo).into(holder.itemView.home_team_logo)
        Glide.with(activity).load(item.awayTeamLogo).into(holder.itemView.visiting_team_logo)

        holder.itemView.setOnClickListener {
          //TODO: Navigate to appropriate match board
        }
    }

    override fun canBindData(item: Any): Boolean {
        return item is NextMatch
    }

    inner class NextMatchViewHolder(itemView: View) : BaseViewHolder<NextMatch>(itemView)
}