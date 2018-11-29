package life.plank.juna.zone.view.LatestMatch

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ahamed.multiviewadapter.BaseViewHolder
import com.ahamed.multiviewadapter.ItemBinder
import kotlinx.android.synthetic.main.football_feed_row.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.League
import life.plank.juna.zone.util.GlobalVariable
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.football.LeagueInfoFragment

class NextMatchBinder internal constructor(private val activity: Activity) : ItemBinder<League, NextMatchBinder.NextMatchViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): NextMatchViewHolder {

        return NextMatchViewHolder(inflater.inflate(R.layout.football_feed_row, parent, false))
    }

    override fun bind(holder: NextMatchViewHolder, item: League) {
        holder.itemView.feed_title_text_view.text = item.name
        //        TODO: Replace with original time to next match
        holder.itemView.kickoff_time.text = ZoneApplication.getContext().getString(R.string.match_status, "2hrs 13 mins")

        holder.itemView.feed_image_view.setImageResource(item.leagueLogo)

        holder.itemView.setOnClickListener {
            GlobalVariable.getInstance().tilePosition = holder.position
            (activity as BaseCardActivity).pushFragment(LeagueInfoFragment.newInstance(item), true)
        }
    }

    override fun canBindData(item: Any): Boolean {
        return item is League
    }

    inner class NextMatchViewHolder(itemView: View) : BaseViewHolder<League>(itemView)
}