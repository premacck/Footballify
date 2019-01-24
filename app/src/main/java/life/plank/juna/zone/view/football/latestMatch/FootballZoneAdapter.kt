package life.plank.juna.zone.view.football.latestMatch

import android.app.Activity
import com.ahamed.multiviewadapter.*
import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.model.football.NextMatch
import life.plank.juna.zone.view.base.addDataManagerAndRegisterBinder
import life.plank.juna.zone.view.common.OnItemClickListener

class FootballZoneAdapter(activity: Activity, restApi: RestApi, onItemClickListener: OnItemClickListener) : RecyclerAdapter() {

    private val topMatchDataManager: DataListManager<NextMatch> = DataListManager(this)
    private val lowerMatchDataManager: DataListManager<NextMatch> = DataListManager(this)
    private val leagueDataManager: DataItemManager<LeagueModel> = DataItemManager(this)

    init {
        addDataManagerAndRegisterBinder(topMatchDataManager, NextMatchBinder(activity, restApi))
        addDataManagerAndRegisterBinder(leagueDataManager, LeagueBinder(onItemClickListener, activity))
        addDataManager(lowerMatchDataManager)
    }

    fun setMatches(nextMatches: List<NextMatch>) {
        if (!isNullOrEmpty(nextMatches)) {
            nextMatches.run {
                topMatchDataManager.set(subList(0, if (size <= 3) size else 3))
                if (size > 3) {
                    lowerMatchDataManager.set(subList(3, size))
                }
            }
        }
        leagueDataManager.setItem(LeagueModel())
    }
}
