package life.plank.juna.zone.view.adapter.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_onboarding.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FootballTeam
import life.plank.juna.zone.util.common.DataUtil.findInt
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp

class TeamSelectionAdapter(
        private val glide: RequestManager,
        private val teamList: MutableList<FootballTeam>
) : androidx.recyclerview.widget.RecyclerView.Adapter<TeamSelectionAdapter.FootballFeedViewHolder>() {

    private var selectedTeams: MutableSet<FootballTeam> = HashSet()
    var selectedTeamNames: MutableSet<String> = HashSet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FootballFeedViewHolder =
            FootballFeedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_onboarding, parent, false))

    override fun onBindViewHolder(holder: FootballFeedViewHolder, position: Int) {
        val footballTeam = teamList[position]
        holder.itemView.title.text = footballTeam.name

        glide.load(footballTeam.logoLink)
                .apply(RequestOptions.centerInsideTransform()
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder))
                .into(holder.itemView.image)

        setItemSelection(footballTeam, holder, false)

        holder.itemView.setOnClickListener {
            setItemSelection(footballTeam, holder, true)
        }
        //TODO: Set card colour after backend returns it
    }

    private fun setItemSelection(footballTeam: FootballTeam, holder: FootballFeedViewHolder, isActionDone: Boolean) {
        holder.itemView.run {
            if (isActionDone) {
                if (selectedTeams.contains(footballTeam)) {
                    selectedTeams.remove(footballTeam)
                    selectedTeamNames.remove(footballTeam.name)
                } else {
                    selectedTeams.add(footballTeam)
                    selectedTeamNames.add(footballTeam.name!!)
                }
            }
            val isItemSelected = selectedTeams.contains(footballTeam)
            follow_image_view.visibility = if (isItemSelected) View.VISIBLE else View.INVISIBLE
            card.alpha = findInt(if (isItemSelected) R.integer.visiblilty_160 else R.integer.opaque).toFloat()
            title.visibility = if (isItemSelected) View.INVISIBLE else View.VISIBLE

            if (card.measuredWidth > 0 && card.measuredHeight > 0) {
                val params = card.layoutParams
                params.width = if (isItemSelected) card.measuredWidth - getDp(14f).toInt() else card.measuredWidth + getDp(14f).toInt()
                params.height = if (isItemSelected) card.measuredHeight - getDp(20f).toInt() else card.measuredHeight + getDp(20f).toInt()
                card.layoutParams = params
            }
        }
    }

    override fun getItemCount(): Int = teamList.size

    fun setTeamList(footballTeams: List<FootballTeam>?) {
        if (isNullOrEmpty(footballTeams)) {
            return
        }
        teamList.clear()
        teamList.addAll(footballTeams as ArrayList)
        notifyDataSetChanged()
    }

    class FootballFeedViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)
}
