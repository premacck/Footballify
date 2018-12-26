package life.plank.juna.zone.view.adapter.board.match.binder

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.*
import android.view.ViewGroup.LayoutParams.*
import android.widget.LinearLayout
import com.ahamed.multiviewadapter.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.synthetic.main.item_line_up.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.*
import life.plank.juna.zone.data.model.binder.LineupsBindingModel
import life.plank.juna.zone.util.common.JunaDataUtil.getIntegratedLineups
import life.plank.juna.zone.util.customview.LineupPlayer
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import org.jetbrains.anko.*

class LineupsBinder(private val glide: RequestManager) : ItemBinder<LineupsBindingModel, LineupsBinder.LineupsViewHolder>() {

    override fun create(inflater: LayoutInflater, parent: ViewGroup): LineupsViewHolder = LineupsViewHolder(inflater.inflate(R.layout.item_line_up, parent, false))

    override fun bind(holder: LineupsViewHolder, item: LineupsBindingModel) {
        doAsync {
            item.lineups = getIntegratedLineups(item.lineups, item.matchEventList)
            uiThread {
                holder.itemView.run {
                    progress_bar.visibility = View.GONE
                    if (item.errorMessage != null) {
                        lineup_center_lines.visibility = View.INVISIBLE
                        no_data.visibility = View.VISIBLE
                        home_team_lineup_layout.visibility = View.GONE
                        visiting_team_lineup_layout.visibility = View.GONE
                        no_data.setText(item.errorMessage!!)
                    } else {
                        lineup_center_lines.visibility = View.VISIBLE
                        no_data.visibility = View.GONE
                        home_team_lineup_layout.visibility = View.VISIBLE
                        visiting_team_lineup_layout.visibility = View.VISIBLE

                        home_team_name.text = item.homeTeam!!.name
                        visiting_team_name.text = item.awayTeam!!.name

                        if (!isNullOrEmpty(item.lineups!!.homeTeamFormation) && !isNullOrEmpty(item.lineups!!.awayTeamFormation)) {
                            home_team_lineup_text.text = item.homeFormation
                            visiting_team_lineup_text.text = item.awayFormation

                            prepareLineup(home_team_lineup_layout, item.lineups!!.homeTeamFormation, findColor(R.color.lineup_player_red), true)
                            prepareLineup(visiting_team_lineup_layout, item.lineups!!.awayTeamFormation, findColor(R.color.purple), false)
                        }
                        loadImage(item.homeTeam?.logoLink, getStartDrawableTarget(home_team_name))
                        loadImage(item.awayTeam?.logoLink, getEndDrawableTarget(visiting_team_name))
                    }
                }
            }
        }
    }

    private fun loadImage(logo: String?, target: SimpleTarget<Drawable>) {
        glide.load(logo).apply(RequestOptions.overrideOf(getDp(14f).toInt(), getDp(14f).toInt())
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder))
                .into(target)
    }

    private fun prepareLineup(lineupLayout: LinearLayout, formationsList: List<FormationList>, labelColor: Int, isHomeTeam: Boolean) {
        lineupLayout.removeAllViews()
        for (formations in if (isHomeTeam) formationsList else formationsList.reversed()) {
            lineupLayout.addView(getLineupLayoutLine(lineupLayout.context, formations, labelColor))
        }
    }

    private fun getLineupLayoutLine(context: Context, formations: List<Formation>, labelColor: Int): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.gravity = Gravity.CENTER
        linearLayout.weightSum = formations.size.toFloat()
        val params = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1f)
        linearLayout.layoutParams = params
        for (formation in formations) {
            val lineupPlayer = LineupPlayer(context, formation, labelColor)
            linearLayout.addView(lineupPlayer)
            (lineupPlayer.layoutParams as LinearLayout.LayoutParams).weight = 1f
        }
        return linearLayout
    }

    override fun canBindData(item: Any): Boolean = item is LineupsBindingModel

    class LineupsViewHolder(itemView: View) : ItemViewHolder<LineupsBindingModel>(itemView)
}